package com.patria.apps.transaction.service.impl;

import com.patria.apps.config.AESHelperService;
import com.patria.apps.entity.Expedition;
import com.patria.apps.entity.Payment;
import com.patria.apps.entity.Transactions;
import com.patria.apps.entity.Product;
import com.patria.apps.entity.ProductReview;
import com.patria.apps.entity.UsersAddress;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.repository.ExpeditionRepository;
import com.patria.apps.repository.PaymentRepository;
import com.patria.apps.repository.TransactionsRepository;
import com.patria.apps.repository.ProductRepository;
import com.patria.apps.repository.ProductReviewRepository;
import com.patria.apps.repository.UsersAddressRepository;
import com.patria.apps.repository.UsersRepository;
import com.patria.apps.response.PaginationResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.transaction.filter.TransactionsFilterService;
import com.patria.apps.transaction.request.TransactionsCreateRequest;
import com.patria.apps.transaction.request.TransactionsUpdateRequest;
import com.patria.apps.transaction.request.TransactionsListRequest;
import com.patria.apps.transaction.request.TransactionsDestroyRequest;
import com.patria.apps.transaction.request.TransactionsPayRequest;
import com.patria.apps.transaction.request.TransactionsProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateProductReviewRequest;
import com.patria.apps.transaction.request.TransactionsUpdateStatusRequest;
import com.patria.apps.transaction.response.TransactionsListResponse;
import com.patria.apps.transaction.serializer.TransactionsSerializerService;
import com.patria.apps.transaction.service.TransactionsService;
import com.patria.apps.vo.StatusTransactionEnum;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final ProductRepository productRepository;
    private final ProductReviewRepository productReviewRepository;
    private final PaymentRepository paymentRepository;
    private final ExpeditionRepository expeditionRepository;
    private final UsersAddressRepository usersAddressRepository;
    private final UsersRepository usersRepository;
    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final TransactionsFilterService transactionsFilterService;
    private final TransactionsSerializerService transactionsSerializerService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("invoiceNum", "invoiceNum");
            put("-invoiceNum", "invoiceNum");
            put("status", "status");
            put("-status", "status");
        }
    };

    @Override
    public ReadResponse<List<TransactionsListResponse>> list(TransactionsListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "createdAt", "desc");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Transactions> transactions = transactionsRepository.findAll(transactionsFilterService.specification(request), pageable);
        List<TransactionsListResponse> usersResponse = transactions
                .getContent()
                .stream()
                .map(datas -> transactionsSerializerService.serialize(
                datas
        ))
                .toList();

        Page<TransactionsListResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                transactions.getTotalElements());

        return ReadResponse.<List<TransactionsListResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(responsePage.getContent())
                .pagination(
                        PaginationResponse.builder()
                                .currentPage(responsePage.getNumber() + 1)
                                .totalPage(responsePage.getTotalPages())
                                .perPage(responsePage.getSize())
                                .total(responsePage.getTotalElements())
                                .count(responsePage.getNumberOfElements())
                                .hasNext(responsePage.hasNext())
                                .hasPrevious(responsePage.hasPrevious())
                                .hasContent(responsePage.hasContent())
                                .build())
                .build();
    }

    @Override
    public ReadResponse createTransactions(TransactionsCreateRequest request) {
        validationHelperService.validate(request);

        if (request.getQty() == 0) {
            throw new GeneralException(HttpStatus.NOT_ACCEPTABLE, "Please add quantity!");
        }

        //Set product
        Long productId = aesService.getDecryptedString(request.getProductKey());
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Product Not Found")
        );

        if (product.getStock() == 0) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Out of stock!");
        }

        Transactions transactions = new Transactions();
        transactions.setProduct(product);
        if (request.getQty() > product.getStock()) {
            transactions.setQty(product.getStock());
        } else {
            transactions.setQty(request.getQty());
        }

        //set payment
        Long paymentId = aesService.getDecryptedString(request.getPaymentKey());
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Payment Not Found")
        );
        transactions.setPayment(payment);

        //set expedition
        Long expeditionId = aesService.getDecryptedString(request.getExpeditionKey());
        Expedition expedition = expeditionRepository.findById(expeditionId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Expedition Not Found")
        );
        transactions.setExpedition(expedition);

        //set address
        Long addressId = aesService.getDecryptedString(request.getUserAddressKey());
        UsersAddress address = usersAddressRepository.findById(addressId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data User Addresss Not Found")
        );
        transactions.setUsersAddress(address);

        transactions.setTotal(Math.round((product.getPrice() * request.getQty()) + expedition.getPrice()));
        transactions.setStatus(StatusTransactionEnum.WAITING_PAYMENT);
        transactions.setInvoiceNum(UUID.randomUUID().toString());
        transactions.setUsers(usersRepository.findById(request.getId()).get());
        transactions.setCreatedAt(LocalDateTime.now());
        transactions.setCreatedBy(request.getId());

        transactionsRepository.save(transactions);
        transactionsRepository.flush();
        
        product.setStock(product.getStock() - transactions.getQty());

        productRepository.save(product);
        productRepository.flush();
                
        return transactionsSerializerService.serializeTransaction(
                "Success add transactions!"
        );
    }

    @Override
    public ReadResponse destroyTransactions(TransactionsDestroyRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Transactions transactions = transactionsRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (!request.getId().equals(transactions.getUsers().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, "You cannot delete this data");
        }

        transactionsRepository.delete(transactions);
        transactionsRepository.flush();

        return transactionsSerializerService.serializeTransaction(
                "Success delete transactions!"
        );
    }

    @Override
    public ReadResponse updateTransactions(TransactionsUpdateRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Transactions transactions = transactionsRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (!request.getId().equals(transactions.getUsers().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, "You cannot update this data");
        }

        //set payment
        Long paymentId = aesService.getDecryptedString(request.getPaymentKey());
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Payment Not Found")
        );
        transactions.setPayment(payment);

        //set address
        Long addressId = aesService.getDecryptedString(request.getUserAddressKey());
        UsersAddress address = usersAddressRepository.findById(addressId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data User Addresss Not Found")
        );
        transactions.setUsersAddress(address);
        transactions.setUpdatedAt(LocalDateTime.now());
        transactions.setUpdatedBy(request.getId());

        transactionsRepository.save(transactions);
        transactionsRepository.flush();

        return transactionsSerializerService.serializeTransaction(
                "Success update transactions!"
        );
    }

    @Override
    public ReadResponse updateStatusTransactions(TransactionsUpdateStatusRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Transactions transactions = transactionsRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        transactions.setStatus(request.getStatus());
        transactions.setUpdatedAt(LocalDateTime.now());
        transactions.setUpdatedBy(request.getId());
        
        transactionsRepository.save(transactions);
        transactionsRepository.flush();
        
        return transactionsSerializerService.serializeTransaction(
                "Success update status transactions!"
        );
    }

    @Override
    public ReadResponse payTransactions(TransactionsPayRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Transactions transactions = transactionsRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        if (!request.getId().equals(transactions.getUsers().getId())) {
            throw new GeneralException(HttpStatus.FORBIDDEN, "You cannot update this data");
        }

        transactions.setStatus(StatusTransactionEnum.ON_DELIVERY);
        transactions.setUpdatedAt(LocalDateTime.now());
        transactions.setUpdatedBy(request.getId());
        
        transactionsRepository.save(transactions);
        transactionsRepository.flush();
        
        return transactionsSerializerService.serializeTransaction(
                "Success pay transactions!"
        );
    }

    @Override
    public ReadResponse productReview(TransactionsProductReviewRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getTransactionKey());
        Transactions transactions = transactionsRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Transaction Not Found")
        );
        
        if(!transactions.getStatus().equals(StatusTransactionEnum.DONE)){
            throw new GeneralException(HttpStatus.NOT_FOUND, "You cannot review this product yet!");
        }
        
        Long productId = aesService.getDecryptedString(request.getProductKey());
        Product product = productRepository.findByIdAndDeletedAtIsNull(productId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Product Not Found")
        );
        
        if(productReviewRepository.findByUsersIdAndProductId(request.getId() ,productId).isPresent()){
            throw new GeneralException(HttpStatus.FORBIDDEN, "You already review this product");
        }
                
        ProductReview productReview = new ProductReview();
        productReview.setScore(request.getScore());
        productReview.setTransactions(transactions);
        productReview.setProduct(product);
        productReview.setReview(request.getReview());
        productReview.setUsers(usersRepository.findById(request.getId()).get());
        productReview.setCreatedAt(LocalDateTime.now());
        productReview.setCreatedBy(request.getId());
        
        productReviewRepository.save(productReview);
        productReviewRepository.flush();
        
        return transactionsSerializerService.serializeTransaction(
                "Success review product!"
        );
    }

    @Override
    public ReadResponse updateProductReview(TransactionsUpdateProductReviewRequest request) {
        validationHelperService.validate(request);
        
        Long id = aesService.getDecryptedString(request.getKey());
        ProductReview productReview = productReviewRepository.findById(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        
        productReview.setScore(request.getScore());
        productReview.setReview(request.getReview());
        productReview.setUpdatedAt(LocalDateTime.now());
        productReview.setUpdatedBy(request.getId());
        
        productReviewRepository.save(productReview);
        productReviewRepository.flush();
        
        return transactionsSerializerService.serializeTransaction(
                "Success update review product!"
        );
    }

    @Override
    public ReadResponse<TransactionsListResponse> retrieveSingleData(String id) {
        
        if(id.isBlank()){
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Id is not found");
        }
        
        Long decryptedId = aesService.getDecryptedString(id);

        Transactions transactions = transactionsRepository.findById(decryptedId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        return transactionsSerializerService.serializeRetrieve(transactions);
        
    }

}
