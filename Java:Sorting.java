Java Sorting

1. Array Sorting

* 오름차순, Default 정렬의 경우 (primitive Type, Object 둘다 가능)
    - Arrays.sort(arr);

* 내림차순의 경우(primitive Type 불가, Object 만 가능)
    - Arrays.sort(arr, Collections.reverseOrder());

    - primitive 타입의 경우 Object 형태로 Wrap 해주고 sorting 한다.

        Character[] charObjectArray = str.chars().mapToObj(c -> (char)c).toArray(Character[]::new);
        Arrays.sort(charObjectArray,Collections.reverseOrder());