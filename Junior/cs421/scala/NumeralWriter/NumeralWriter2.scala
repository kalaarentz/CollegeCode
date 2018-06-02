package LearnScala

/**
  * Created by kalaarentz on 4/8/17.
  */
object NumeralWriter2 {

  private var num:Long = 0.toLong

  private var numNames = Array( "", "one", "two", "three",
    "four", "five", "six", "seven", "eight", "nine",
    "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
  )

  private var tensNames = Array( "", "ten", "twenty", "thirty", "forty",
    "fifty", "sixty", "seventy", "eighty", "ninety"
  )

  def main(args: Array[String]): Unit = {
  }

  def numeral(n:Long): String = {
    num = n;
    if ( num == 0 ) {
      return "zero"
    } else if ( num < 0 ) {
      num *= (-1)
      return "minus " + printNums()
    } else {
      return printNums()
    }
  }

  def printNums(): String = {
    var result: String = ""

    var numToString: String = num.toString

    while( numToString.length < 21 )  {
      numToString = "0" + numToString
    }

    // 21
    var quintillion: Int = Integer.parseInt(numToString.substring(0,3))
    // 18
    var quadrillion: Int = Integer.parseInt(numToString.substring(3,6))
    // 15
    var trillion: Int = Integer.parseInt(numToString.substring(6,9))
    // 12
    var billion: Int = Integer.parseInt(numToString.substring(9,12))
    // 9
    var million: Int = Integer.parseInt(numToString.substring(12,15))
    // 6
    var hundredThousand: Int = Integer.parseInt(numToString.substring(15,18))
    // 3
    var thousand: Int = Integer.parseInt(numToString.substring(18,21))


    result += splitNums( quintillion, " quintillion ")
    result += splitNums( quadrillion, " quadrillion ")
    result += splitNums( trillion, " trillion ")
    result += splitNums( billion, " billion ")
    result += splitNums( million, " million ")
    result += splitNums( hundredThousand, " thousand " )
    result += convertNumThousands(thousand.toLong)

    return result
  }

  def splitNums(splitNumber:Long , valStr: String): String = {

    var retStr: String = null
    if( splitNumber == 0 ) {
      retStr = ""
    } else {
      retStr = convertNumThousands( splitNumber.toInt ) + valStr
    }

    return retStr
  }

  def convertNumThousands(n: Long): String = {

    var str: String = null;
    var number: Int = n.toInt

    if( number % 100 < 20 ) {
      str = numNames(number % 100)
      number /= 100
    } else {
      if( number % 10 != 0 ) {
        str = numNames(number % 10)
        number /= 10
        str = tensNames(number % 10) + "-" + str
        number /= 10
      } else {
        str = numNames(number % 10)
        number /= 10
        str = tensNames(number % 10) + str
        number /= 10
      }
    }

    if (number == 0) return str
    return numNames(number) + " hundred " + str
  }

}
