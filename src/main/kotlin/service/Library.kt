package service

import entity.Book
import entity.BookStatus
import entity.Borrow
import repository.*
import java.sql.Date
import java.util.concurrent.TimeUnit

class Library(dataSource: DataSource) {
    private val memberRepository = MemberRepository(dataSource)
    private val bookRepository = BookRepository(dataSource)
    private val borrowRepository = BorrowRepository(dataSource)

    fun getCurBorrowCntOfMember(memberId: Int): Int {
        return borrowRepository.findAllByMemberId(memberId).filter { it.returnDate == null }.size
    }

    fun getStatusOfBook(serialNumber: Int): BookStatus {
        return if (borrowRepository.findAllBySerialNumber(serialNumber)
                .any { it.returnDate == null }
        ) BookStatus.BORROWED
        else BookStatus.REMAIN
    }

    fun hasOverdueBorrowBook(memberId: Int): Boolean {
        return borrowRepository.findAllByMemberId(memberId).any {
            it.borrowStartDate.before(
                Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis((14 + it.countOfDueDateExtension * 7).toLong()))
            )
        }
    }

    fun printAllBooks() {
        bookRepository.printAll().forEach(::println)
    }

    fun borrow(memberId: Int, serialNumber: Int) {
        val member = memberRepository.findByMemberId(memberId)
            ?: throw Exception("member doesn't exist")
        val book = bookRepository.findBySerialNumber(serialNumber)
            ?: throw Exception("book doesn't exist")

        if (hasOverdueBorrowBook(memberId)) throw Exception("연체된 도서로 인해 대출이 제한됩니다")

        if (getStatusOfBook(serialNumber) != BookStatus.REMAIN) throw Exception("대출 불가능한 도서")

        val curBorrowCntOfMember = getCurBorrowCntOfMember(memberId)
        if (curBorrowCntOfMember >= member.job.maxBorrowCnt) throw Exception("대출 가능 횟수 초과")

        borrowRepository.save(
            Borrow(
                memberId = memberId,
                serialNumber = serialNumber,
                borrowStartDate = Date(System.currentTimeMillis()),
            )
        )
    }

    fun `return`(memberId: Int, serialNumber: Int) {
        val member = memberRepository.findByMemberId(memberId)
            ?: throw Exception("member doesn't exist")
        val book = bookRepository.findBySerialNumber(serialNumber)
            ?: throw Exception("book doesn't exist")
        if (!borrowRepository.findByMemberIdAndSerialNumber(memberId, serialNumber).any() { it.returnDate == null })
            throw Exception("해당 회원이 대출하지 않은 도서")

        borrowRepository.returnBook(memberId, serialNumber, Date(System.currentTimeMillis()))
    }

    fun curBorrowRecord(memberId: Int, serialNumber: Int): Borrow? {
        val list =
            borrowRepository.findByMemberIdAndSerialNumber(memberId, serialNumber).filter() { it.returnDate == null }
                .toList()
        return if (list.isEmpty()) null else list[0]
    }

    fun extendBorrowDuration(memberId: Int, serialNumber: Int) {
        val member = memberRepository.findByMemberId(memberId)
            ?: throw Exception("member doesn't exist")
        val book = bookRepository.findBySerialNumber(serialNumber)
            ?: throw Exception("book doesn't exist")
        val borrow = curBorrowRecord(memberId, serialNumber)
            ?: throw Exception("해당 회원이 대출하지 않은 도서")

        if (borrow.countOfDueDateExtension > 0) throw Exception("연장은 최대 1회만 가능합니다")
        borrowRepository.extendBorrowDuration(memberId, serialNumber)
    }
}