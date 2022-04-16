package examples;

import java.util.Collection;
import java.util.List;

public class FirstExample {
    private int getMaxLengthOfValues(List<DataSet> dataSets) {
        return dataSets.stream()
                .map(DataSet::getValues)
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .mapToInt(String::length)
                .max()
                .stream()
                .map(value -> Math.max(value, getMaxLengthOfColumnNames(dataSets)))
                .max()
                .orElse(0);


        //return dataSets.stream()
        //        .map(DataSet::getValues)
        //        .flatMap(Collection::stream)
        //        .map(String::valueOf)
        //        .collect(Collectors
        //                .collectingAndThen(Collectors.toList(),
        //                        f -> Stream.concat(f.stream(), dataSets.get(0).getColumnNames().stream())))
        //        .mapToInt(String::length)
        //        .max()
        //        .orElse(0);

        //if dataSets not empty :
        //return Stream.concat(dataSets.stream()
        //        .map(DataSet::getValues)
        //        .flatMap(Collection::stream)
        //        .map(String::valueOf),
        //        dataSets.get(0).getColumnNames().stream())
        //        .mapToInt(String::length)
        //        .max()
        //        .orElse(0);

    }

    private int getMaxLengthOfDataSetValues(List<DataSet> dataSets) {
        return dataSets.stream()
                .map(DataSet::getValues)
                .flatMap(Collection::stream)
                .map(String::valueOf)
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    private int getMaxLengthOfColumnNames(List<DataSet> dataSets) {
        return dataSets.get(0).getColumnNames().stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }
}
