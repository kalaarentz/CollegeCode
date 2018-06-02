package cs421scala

/**
  * Created by kalaarentz on 4/24/17.
  */
object Main {
  def main(args: Array[String]) {
    var singleQueuePriorityQueue = new SingleQueuePriorityQueue[String](5)

    singleQueuePriorityQueue.push("Hello", 3)
    singleQueuePriorityQueue.push("What", 2)
    singleQueuePriorityQueue.push("Up", 4)
    singleQueuePriorityQueue.push("Homie", 1)
    singleQueuePriorityQueue.push("Apple", 4 )
    singleQueuePriorityQueue.push("Cat", 1)
    singleQueuePriorityQueue.push("Dog", 3)
    singleQueuePriorityQueue.push("Orange", 2)
    singleQueuePriorityQueue.push("Orange", 0)
    singleQueuePriorityQueue.push("Orange", 4)

    //singleQueuePriorityQueue.print()

    //println( singleQueuePriorityQueue.copy() )

    while( !singleQueuePriorityQueue.isEmpty ) {
      println( singleQueuePriorityQueue.pop() )
    }

//    var queueArrayPriorityQueue = new QueueArrayPriorityQueue[String](5)
//
//    println(queueArrayPriorityQueue.isEmpty)
//    queueArrayPriorityQueue.push("Hello", 3)
//    queueArrayPriorityQueue.push("What", 2)
//    queueArrayPriorityQueue.push("Up", 4)
//    queueArrayPriorityQueue.push("Homie", 1)
//    queueArrayPriorityQueue.push("Apple", 4)
//    queueArrayPriorityQueue.push("Cat", 1)
//    queueArrayPriorityQueue.push("Dog", 3)
//    queueArrayPriorityQueue.push("Orange", 2)
//    queueArrayPriorityQueue.push("Orange", 0)
//    queueArrayPriorityQueue.push("Orange", 4)
//
//    println(queueArrayPriorityQueue.isEmpty)
//
//    while( !queueArrayPriorityQueue.isEmpty ) {
//      println(queueArrayPriorityQueue.pop() )
//    }

  }
}
