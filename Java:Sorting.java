Java Sorting

1. Array Sorting

* 오름차순, Default 정렬의 경우 (primitive Type, Object 둘다 가능)
    - Arrays.sort(arr);

* 내림차순의 경우(primitive Type 불가, Object 만 가능)
    - Arrays.sort(arr, Collections.reverseOrder());

    - primitive 타입의 경우 Object 형태로 Wrap 해주고 sorting 한다.

        Character[] charObjectArray = str.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Arrays.sort(charObjectArray,Collections.reverseOrder());


2. Map sorting
/*https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values*/
* Value 오름차순
Stream<Map.Entry<K,V>> sorted =
                                map.entrySet().stream()
                                   .sorted(Map.Entry.comparingByValue());

* Value 내림차순
Stream<Map.Entry<K,V>> sorted =
                                map.entrySet().stream()
                                   .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));