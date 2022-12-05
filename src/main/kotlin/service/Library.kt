package service

import entity.BookStatus
import entity.Borrow
import repository.BookRepository
import repository.BorrowRepository
import repository.DataSource
import repository.MemberRepository
import java.sql.Date

class Library(dataSource: DataSource) {
    private val memberRepository = MemberRepository(dataSource)
    private val bookRepository = BookRepository(dataSource)
    private val borrowRepository = BorrowRepository(dataSource)

    fun getCurBorrowCntOfMember(memberId: Int): Int {
        return borrowRepository.findAllByMemberId(memberId).filter { it.returnDate == null }.size
    }

    fun getStatusOfBook(serialNumber: Int): BookStatus {
        return if(borrowRepository.findAllBySerialNumber(serialNumber).any { it.returnDate == null }) BookStatus.BORROWED
        else  BookStatus.REMAIN
    }

    fun borrow(memberId: Int, serialNumber: Int) {
        val member = memberRepository.findByMemberId(memberId)
            ?: throw Exception("member doesn't exist")
        val book = bookRepository.findBySerialNumber(serialNumber)
            ?: throw Exception("book doesn't exist")

        if(getStatusOfBook(serialNumber) != BookStatus.REMAIN) throw Exception("대출 불가능한 도서")

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
}