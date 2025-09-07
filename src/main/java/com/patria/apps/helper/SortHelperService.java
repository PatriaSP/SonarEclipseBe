package com.patria.apps.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortHelperService {

    public Sort setSort(Map<String, String> mapSort, String sort, String defaultSort) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sort == null) {
            orders.add(Sort.Order.asc(defaultSort));
        } else {
            for (Map.Entry<String, String> entry : mapSort.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(sort)) {
                    String[] sorts = entry.getValue().split(",");
                    if (sorts.length == 1) {
                        if (sort.contains("-")) {
                            orders.add(Sort.Order.desc(entry.getValue().trim()));
                        } else {
                            orders.add(Sort.Order.asc(entry.getValue().trim()));
                        }
                    } else {
                        for (String sortData : sorts) {
                            if (sort.contains("-")) {
                                orders.add(Sort.Order.desc(sortData.trim()));
                            } else {
                                orders.add(Sort.Order.asc(sortData.trim()));
                            }
                        }
                    }

                }
            }
        }
        return Sort.by(orders);
    }

    public Sort setSort(Map<String, String> mapSort, String sort, String defaultSort, String defaultOrder) {

        List<Sort.Order> orders = new ArrayList<>();

        if (sort == null) {
            if (defaultOrder == null) {
                orders.add(Sort.Order.asc(defaultSort));
            } else {
                orders.add(defaultOrder.equalsIgnoreCase("asc") ? Sort.Order.asc(defaultSort) : Sort.Order.desc(defaultSort));
            }
        } else {
            for (Map.Entry<String, String> entry : mapSort.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(sort)) {
                    String[] sorts = entry.getValue().split(",");
                    if (sorts.length == 1) {
                        if (sort.contains("-")) {
                            orders.add(Sort.Order.desc(entry.getValue().trim()));
                        } else {
                            orders.add(Sort.Order.asc(entry.getValue().trim()));
                        }
                    } else {
                        for (String sortData : sorts) {
                            if (sort.contains("-")) {
                                orders.add(Sort.Order.desc(sortData.trim()));
                            } else {
                                orders.add(Sort.Order.asc(sortData.trim()));
                            }
                        }
                    }

                }
            }
        }
        return Sort.by(orders);
    }
}
