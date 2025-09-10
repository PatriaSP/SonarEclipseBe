package com.patria.apps.users.service.impl;

import com.patria.apps.config.AESHelperService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.patria.apps.helper.JWTHelperService;
import com.patria.apps.entity.Users;
import com.patria.apps.entity.UsersAddress;
import com.patria.apps.exception.GeneralException;
import com.patria.apps.helper.SecurityHelperService;
import com.patria.apps.helper.SortHelperService;
import com.patria.apps.helper.ValidationHelperService;
import com.patria.apps.repository.RoleRepository;
import com.patria.apps.response.JWTResponse;
import com.patria.apps.response.PaginationResponse;
import com.patria.apps.response.ReadResponse;
import com.patria.apps.users.filter.UsersFilterService;
import com.patria.apps.users.request.UserCreateRequest;
import com.patria.apps.users.request.UserDeleteRequest;
import com.patria.apps.users.request.UserDestroyRequest;
import com.patria.apps.users.request.UserListRequest;
import com.patria.apps.users.request.UserLoginRequest;
import com.patria.apps.users.request.UserPasswordChangeRequest;
import com.patria.apps.users.request.UserRestoreRequest;
import com.patria.apps.users.request.UserRetrieveRequest;
import com.patria.apps.users.request.UserUpdateRequest;
import com.patria.apps.users.response.UsersResponse;
import com.patria.apps.users.serializer.UsersSerializerService;
import com.patria.apps.users.service.UsersService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.patria.apps.repository.UsersRepository;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final SortHelperService sortHelperService;
    private final ValidationHelperService validationHelperService;
    private final AESHelperService aesService;
    private final UsersFilterService usersFilterService;
    private final UsersSerializerService usersSerializerService;
    private final UsersRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTHelperService jwtHelperService;

    private final Map<String, String> sortMap = new HashMap<String, String>() {
        {
            //parameter and column name
            put("username", "username");
            put("-username", "username");
            put("name", "userDetail.firstName, userDetail.lastName");
            put("-name", "userDetail.firstName, userDetail.lastName");
        }
    };

    private Date getDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    @Override
    public JWTResponse login(UserLoginRequest request) {
        validationHelperService.validate(request);

        Users user = userRepository
                .findByEmail(request.getUsername())
                .orElse(null);
        if (Objects.isNull(user)) {
            user = userRepository
                    .findByUsername(request.getUsername())
                    .orElseThrow(() -> new GeneralException(HttpStatus.UNAUTHORIZED, "Username or Password is wrong"));
        }

        boolean checkPassword = BCrypt.checkpw(request.getPassword(), user.getPassword());

        if (checkPassword) {
            Long expired = 1L;
            String jwtToken = jwtHelperService.generateToken(user.getId(), expired);

            Date date = getDate(LocalDate.now().plusDays(expired));

            user.setLastLoginAt(LocalDateTime.now());
            userRepository.save(user);

            return JWTResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Login Success")
                    .accessToken(jwtToken)
                    .date(date)
                    .build();
        } else {
            throw new GeneralException(HttpStatus.UNAUTHORIZED, "Username or Password is wrong");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Users> user = this.userRepository.findByUsername(username);
        return user.orElseThrow(() -> new GeneralException(HttpStatus.UNAUTHORIZED, "Not Authorized"));
    }

    @Override
    public ReadResponse<List<UsersResponse>> listUser(UserListRequest request) {
        Sort sortBy = sortHelperService.setSort(sortMap, request.getSort(), "username");
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getPerPage(), sortBy);
        Page<Users> users = userRepository.findAll(usersFilterService.specification(request), pageable);
        List<UsersResponse> usersResponse = users
                .getContent()
                .stream()
                .map(datas -> usersSerializerService.serialize(
                datas
        ))
                .toList();

        Page<UsersResponse> responsePage = new PageImpl<>(usersResponse, pageable,
                users.getTotalElements());

        return ReadResponse.<List<UsersResponse>>builder()
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
    public ReadResponse delete(HttpServletRequest servletRequest, UserDeleteRequest request, Users deletedBy) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Users user = userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        user.setIsActive(Boolean.FALSE);
        user.setDeletedBy(deletedBy.getId());
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return usersSerializerService.serializeTransaction(
                "Success delete user!"
        );
    }

    @Override
    public ReadResponse restore(HttpServletRequest servletRequest, UserRestoreRequest request, Users updatedBy) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Users user = userRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        user.setDeletedAt(null);
        user.setDeletedBy(null);
        user.setIsActive(Boolean.TRUE);
        user.setUpdatedBy(updatedBy.getId());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return usersSerializerService.serializeTransaction(
                "Data successfully restored!"
        );
    }

    @Override
    public ReadResponse destroy(UserDestroyRequest request) {
        validationHelperService.validate(request);

        Long id = aesService.getDecryptedString(request.getKey());
        Users user = userRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        Long userId = user.getId();
        user.setId(userId);
        userRepository.delete(user);

        return usersSerializerService.serializeTransaction(
                "Data permanently deleted!"
        );
    }

    @Override
    public ReadResponse<UsersResponse> retrieveSingleData(String id) {
        
        if (id.isBlank()) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Id is not found");
        }
        
        Long decryptedId = aesService.getDecryptedString(id);

        Users user = userRepository.findById(decryptedId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );
        return usersSerializerService.serializeRetrieve(user);
    }

    @Override
    public ReadResponse update(HttpServletRequest servletRequest, UserUpdateRequest request, Users updatedBy) {

        validationHelperService.validate(request);

        Long decryptedId = aesService.getDecryptedString(request.getKey());

        Users users = userRepository.findById(decryptedId).orElseThrow(
                () -> new GeneralException(HttpStatus.NOT_FOUND, "Data Not Found")
        );

        if (userRepository.findByEmail(request.getEmail()).isPresent() && !request.getEmail().equalsIgnoreCase(users.getEmail())) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Email is already exist!");
        }

        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setPhone(request.getPhone().toString());
        users.setEmail(request.getEmail());
        users.setCountry(request.getCountry().toString());

        if (users.getUsersAddress() == null) {
            users.setUsersAddress(new ArrayList<>());
        }

        users.getUsersAddress().clear();

        for (String address : request.getAddress()) {
            UsersAddress addr = new UsersAddress();
            addr.setAddress(address);
            addr.setUsers(users);
            users.getUsersAddress().add(addr);
        }

        users.setUpdatedBy(updatedBy.getId());
        users.setUpdatedAt(LocalDateTime.now());

        users.setIsActive(request.isActive());

        userRepository.save(users);
        userRepository.flush();

        return usersSerializerService.serializeTransaction(
                "Success update account!"
        );
    }

    @Override
    public ReadResponse create(HttpServletRequest servletRequest, UserCreateRequest request) {

        validationHelperService.validate(request);

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Username is already exist!");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Email is already exist!");
        }

        Users user = SecurityHelperService.getPrincipal();
        Users users = new Users();
        users.setFirstName(request.getFirstName());
        users.setLastName(request.getLastName());
        users.setUsername(request.getUsername());
        users.setPhone(request.getPhone().toString());
        users.setEmail(request.getEmail());
        users.setCountry(request.getCountry().toString());

        List<UsersAddress> usersAddress = new ArrayList<>();
        for (String address : request.getAddress()) {
            UsersAddress addr = new UsersAddress();
            addr.setAddress(address);
            addr.setUsers(users);
            usersAddress.add(addr);
        }
        users.setUsersAddress(usersAddress);

        users.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        users.setCreatedAt(LocalDateTime.now());
        users.setCreatedBy(0L);
        users.setIsActive(true);
        if (user == null) {
            users.setRole(roleRepository.findById(2L).get());
        } else {
            if (user.getRole().getRoleName().equalsIgnoreCase("admin")) {
                users.setRole(user.getRole());
            } else {
                throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Only administrator can add user!");
            }
        }

        userRepository.save(users);
        userRepository.flush();

        return usersSerializerService.serializeTransaction(
                "Success create account!"
        );
    }

    @Override
    public ReadResponse updatePassword(HttpServletRequest servletRequest, UserPasswordChangeRequest request, Users updatedBy) {
        validationHelperService.validate(request);

        Long decryptedId = aesService.getDecryptedString(request.getKey());

        Users user = userRepository.findById(decryptedId)
                .orElseThrow(
                        () -> new GeneralException(HttpStatus.UNAUTHORIZED, "User not exist")
                );

        boolean checkOldPassword = BCrypt.checkpw(request.getCurrentPassword(), user.getPassword());
        if (!checkOldPassword) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Your previous password is wrong");
        }

        boolean checkPassword = BCrypt.checkpw(request.getPassword(), user.getPassword());
        if (checkPassword) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Please Enter a new password that is different from the previous password");
        } else {
            String hashPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
            user.setPassword(hashPassword);
        }

        user.setUpdatedBy(user.getId());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return usersSerializerService.serializeTransaction(
                "Success change password!"
        );
    }

}
