object Main {
  
  def main( args: Array[String]) {
    val number = Long.MaxValue
    println( number )

    val numeralWriter = new NumeralWriter
    val numberString = numeralWriter.numeral(number)

    println( numberString )
  }
}
