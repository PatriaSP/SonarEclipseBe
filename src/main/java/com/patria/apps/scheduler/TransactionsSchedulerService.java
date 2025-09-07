package com.patria.apps.scheduler;

import com.patria.apps.entity.ExpeditionHistory;
import com.patria.apps.entity.Product;
import com.patria.apps.entity.Transactions;
import com.patria.apps.entity.Users;
import com.patria.apps.helper.MailHelperService;
import com.patria.apps.helper.ThymeleafHelperService;
import com.patria.apps.repository.TransactionsRepository;
import com.patria.apps.vo.StatusTransactionEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.patria.apps.repository.ExpeditionHistoryRepository;
import com.patria.apps.repository.ProductRepository;
import com.patria.apps.repository.UsersRepository;
import com.patria.apps.response.MonthlyReport;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionsSchedulerService {

    private final TransactionsRepository transactionsRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;
    private final ExpeditionHistoryRepository expeditionHistoryRepository;
    private final MailHelperService mailHelperService;
    private final ThymeleafHelperService thymeleafHelperService;

    @Scheduled(cron = "0 * * * * ?")
    public void transactionDeliveryScheduler() {
        List<Transactions> transactions = transactionsRepository.findByStatus(StatusTransactionEnum.ON_DELIVERY);
        for (Transactions data : transactions) {
            ExpeditionHistory expeditionHistory = new ExpeditionHistory();
            expeditionHistory.setDetail("Delivered to recepient");
            expeditionHistory.setTransactions(data);
            expeditionHistory.setExpedition(data.getExpedition());
            expeditionHistory.setCreatedAt(LocalDateTime.now());
            expeditionHistory.setCreatedBy(0L);

            expeditionHistoryRepository.save(expeditionHistory);
            expeditionHistoryRepository.flush();

            data.setStatus(StatusTransactionEnum.DONE);

            transactionsRepository.save(data);
            transactionsRepository.flush();

            //Email
            Map<String, Object> dataMail = new HashMap<>();
            dataMail.put("name", data.getUsers().getFirstName() + " " + data.getUsers().getLastName());
            dataMail.put("invoiceNum", data.getInvoiceNum());
            dataMail.put("detail", "Delivered to reception. Signed by: " + data.getUsers().getFirstName() + " " + data.getUsers().getLastName());
            dataMail.put("date", LocalDateTime.now());
            dataMail.put("total", new BigDecimal(data.getTotal()));
            dataMail.put("expedition", data.getExpedition().getExpeditionName());
            dataMail.put("address", data.getUsersAddress().getAddress());
            dataMail.put("productName", data.getProduct().getName());
            dataMail.put("price", new BigDecimal(data.getProduct().getPrice()));
            dataMail.put("qty", new BigDecimal(data.getQty()));

            String message = thymeleafHelperService.renderTemplateAsString("delivered-message", dataMail);

            mailHelperService.send(data.getUsers().getEmail(), message, "📦 Delivery Confirmation - Invoice " + data.getInvoiceNum());
        }
    }

    @Scheduled(cron = "0 * * * * ?")
    public void monthlyReportScheduler() {
        List<Users> users = usersRepository.findByRole_RoleName("Admin");
        YearMonth currentMonth = YearMonth.now();
        List<Product> products = productRepository.findAll();

        //Email
        Map<String, Object> dataMail = buildMonthlyReport(products, currentMonth);

        String message = thymeleafHelperService.renderTemplateAsString("monthly-report", dataMail);

        for (Users data : users) {

            mailHelperService.send(data.getEmail(), message, "📊 Sonar Eclipse Monthly Report - " + currentMonth);
        }
    }

    public Map<String, Object> buildMonthlyReport(List<Product> products, YearMonth month) {
        List<MonthlyReport> rows = new ArrayList<>();
        int totalSold = 0;
        double totalRevenue = 0;

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        for (Product p : products) {
            int sold = 0;
            long total = 0L;

            for (Transactions tx : p.getTransactions()) {
                if (tx.getCreatedAt() != null
                        && !tx.getCreatedAt().toLocalDate().isBefore(start)
                        && !tx.getCreatedAt().toLocalDate().isAfter(end)
                        && !tx.getStatus().equals(StatusTransactionEnum.WAITING_PAYMENT)) {

                    sold += tx.getQty();
                    total += tx.getTotal();
                }
            }

            int remaining = p.getStock();

            rows.add(new MonthlyReport(
                    p.getName(),
                    remaining,
                    sold,
                    total
            ));

            totalSold += sold;
            totalRevenue += total;
        }

        Map<String, Object> model = new HashMap<>();
        model.put("date", month);
        model.put("products", rows);
        model.put("total", totalSold);
        model.put("totalRevenue", totalRevenue);
        return model;
    }
}
