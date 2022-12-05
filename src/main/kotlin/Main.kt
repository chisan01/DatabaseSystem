import repository.DataSource
import service.Library

fun main(args: Array<String>) {
    val dataSource = DataSource(
        "192.168.55.189",
        "4567",
        "library",
        "1234"
    )
    val library = Library(dataSource)

    println("---------- 도서관 프로그램 -----------")
    while (true) {
        println("1) 모든 책 목록 보기")
        println("2) 책 대출하기")
        println("3) 책 반납하기")
        println("4) 책 대출 기한 연장하기")
        println("5) 프로그램 종료")
        print("(1) ~ (5) 중 원하는 메뉴를 선택하세요: ")
        val menu = readLine()?.toInt()
        when (menu) {
            1 -> {
                library.printAllBooks()
            }
            2 -> {
                print("회원 ID: ")
                val memberId = readLine()?.toInt() ?: 0
                print("대출하려는 책 ID: ")
                val serialNumber = readLine()?.toInt() ?: 0
                try {
                    library.borrow(memberId, serialNumber)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            3 -> {
                print("회원 ID: ")
                val memberId = readLine()?.toInt() ?: 0
                print("반납하려는 책 ID: ")
                val serialNumber = readLine()?.toInt() ?: 0
                try {
                    library.`return`(memberId, serialNumber)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            4 -> {
                print("회원 ID: ")
                val memberId = readLine()?.toInt() ?: 0
                print("대출 연장하려는 책 ID: ")
                val serialNumber = readLine()?.toInt() ?: 0
                try {
                    library.extendBorrowDuration(memberId, serialNumber)
                } catch (e: Exception) {
                    println(e.message)
                }
            }
            5 -> return
            else -> {
                println("올바른 메뉴를 입력해주세요")
            }
        }
        println()
    }
}