/*
개요
Comparable, Comparator 모두 인터페이스로, 객체 정렬 또는 이진트리를 구성한다.

Comparable - 인터페이스를 구현한 <객체 스스로에게 부여하는 한가지 기본 정렬> 규칙을 설정하는 목적

Comparator - 인터페이스를 구현한 클래스는 <정렬 규칙 그 자체를 의미>, 기본 정렬 규칙과 다르게 원하는 대로 정렬 순서를
지정하고 싶을때 사용한다.
*/


public class ComparatorVsComparable {

    //Comparable 구현, 스스로에게 부여하는 정렬
    class Book implements Comaparable<Book> {

        private int price;

        public Book(int price) {
            this.price = price;
        }

        public int getPrice() {
            return this.price;
        }

        @Override
        public int compareTo(Book o) {
            return this.price - o.price; // this(자신)이 앞에 있는게 오름차순  -> 여기서는 가격의 오름 차순으로 정렬된다.
        }

        //Comparator 구현, 하나의 정렬 방식 여러개의 Comparator가 존재할 수 있다.
        public static Comparator<Book> bookComparator = new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getPrice() - o2.getPrice(); // 첫번째 파라미터가 앞에 있는게 오름차순
            }
        }
    }

    public static void main(String[] args) {

        //Anonumous Comparator
        Collections.sort(myBookList, new Comparator<Book>() {
                    @Override
                    public int comapre(Book o1, Book o2) {
                        return o1.getPrice()-o2.getPrice();
                    }
                }

        //Lambda Comparator
        Collections.sort(myBookList, (Book b1, Book b2) -> b1.getPrice()-b2.getPrice());

    }
}

