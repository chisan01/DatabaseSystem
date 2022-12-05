package acceptanceTest

import entity.Book
import entity.BookInfo
import entity.Job
import entity.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.*
import service.Library

class AcceptanceTest {

    private val testDataSource = DataSource(
        "localhost",
        "3306",
        "library",
        "1234"
    )

    private val library = Library(testDataSource)

    private val memberRepository = MemberRepository(testDataSource)
    private val bookInfoRepository = BookInfoRepository(testDataSource)
    private val bookRepository = BookRepository(testDataSource)
    private val borrowRepository = BorrowRepository(testDataSource)

    lateinit var universityStudent: Member
    lateinit var graduateStudent: Member
    lateinit var professor: Member

    val bookInfos = mutableListOf<BookInfo>()
    val books = mutableListOf<Book>()

    @BeforeEach
    internal fun setUp() {
        testDataSource.deleteAll()

        universityStudent =
            memberRepository.save(Member(name = "test", password = "1234", job = Job.UNIVERSITY_STUDENT))
        graduateStudent =
            memberRepository.save(Member(name = "test", password = "1234", job = Job.GRADUATE_STUDENT))
        professor =
            memberRepository.save(Member(name = "test", password = "1234", job = Job.PROFESSOR))

        bookInfos.clear()
        books.clear()
        repeat(5) {
            val bookInfo = bookInfoRepository.addBookInfo(
                BookInfo(
                    title = "test$it",
                    author = "chisan",
                    publisher = "출판사",
                    publishYear = 2022
                )
            )
            bookInfos.add(bookInfo)

            repeat(2) {
                val book = bookRepository.save(Book(bookNumber = bookInfo.bookNumber!!))
                books.add(book)
            }
        }
    }

    @Test
    fun `대출 가능한 상태의 도서만 대출할 수 있다`() {

    }

    @Test
    fun `학부생은 동시에 최대 1권까지 대출 가능`() {
        // given
        library.borrow(universityStudent.memberId!!, books[0].serialNumber!!)
        // when
        val thrown = Assertions.catchThrowable {
            library.borrow(universityStudent.memberId!!, books[3].serialNumber!!)
        }
        // then
        Assertions.assertThat(thrown).isInstanceOf(Exception::class.java).hasMessageContaining("대출 가능 횟수 초과")
        Assertions.assertThat(borrowRepository.findAllByMemberId(universityStudent.memberId!!).size).isEqualTo(1)
    }

    @Test
    fun `대학원생은 동시에 최대 2권까지 대출 가능`() {
        // given
        for(i in 0..1) {
            library.borrow(graduateStudent.memberId!!, books[i].serialNumber!!)
        }
        // when
        val thrown = Assertions.catchThrowable {
            library.borrow(graduateStudent.memberId!!, books[2].serialNumber!!)
        }
        // then
        Assertions.assertThat(thrown).isInstanceOf(Exception::class.java).hasMessageContaining("대출 가능 횟수 초과")
        Assertions.assertThat(borrowRepository.findAllByMemberId(graduateStudent.memberId!!).size).isEqualTo(2)
    }

    @Test
    fun `교수는 동시에 최대 5권까지 대출 가능`() {

    }

    @Test
    fun `도서는 대출 시작일로부터 2주 안에 반납`() {

    }

    @Test
    fun `반납일 1일 전부터 대출 연장 가능`() {

    }

    @Test
    fun `대출 연장시 반납일 1주일 연장`() {

    }

    @Test
    fun `대출 연장은 대출마다 최대 1회 가능`() {

    }

    @Test
    fun `반납일을 경과할 시 도서를 반납할때까지 도서관 이용이 제한된다`() {

    }

    @Test
    fun `반납일을 경과해서 도서를 반납한 경우, 경과된 기간만큼 도서관 이용이 제한된다`() {

    }

    @Test
    fun `모든 책이 대출 상태인 경우, 대출을 희망하는 회원은 해당 도서를 예약할 수 있다`() {

    }

    @Test
    fun `동일한 도서에 대해서 회원당 최대 한번의 예약이 가능하다`() {

    }

    @Test
    fun `예약한 도서가 대출 가능한 상태가 되었을때부터 3일간 대출을 우선적으로 할 수 있다`() {

    }

    @Test
    fun `여러 명이서 동일한 책에 대해서 예약을 진행한 경우, 예약이 빠른 사람부터 차례대로 기회가 돌아가고 앞에 몇 사람이 남아있는지 확인이 가능하다`() {

    }

    @Test
    fun `책 제목을 기준으로 책을 검색할 수 있다`() {

    }

    @Test
    fun `저자명을 기준으로 책을 검색할 수 있다`() {

    }

    @Test
    fun `회원은 관심 도서 목록을 관리할 수 있다`() {

    }

    @Test
    fun `도서별 대출건수에 따라 월별 인기 도서 순위가 정해진다`() {

    }

    @Test
    fun `회원별 대출 건수에 따라 월별 이용자 순위가 정해진다`() {

    }

    @Test
    fun `회원이 책에 손상을 가한 채로 반납할시 해당 회원에게 경고가 누적된다`() {

    }

    @Test
    fun `손상된 책은 더 이상 대출이 가능하지 않다`() {

    }

    @Test
    fun `경고가 총 3회 누적된 회원은 도서관 이용이 영구적으로 제한된다`() {

    }
}