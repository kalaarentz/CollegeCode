package cs421scala

import scala.collection.mutable.{ArrayBuffer, Queue}

/** An implementation of {@link cs421scala.PriorityQueue PriorityQueue}
  * using an array of Scala {@link scala.collection.mutable.Queue Queue}
  * instances, one {@link scala.collection.mutable.Queue Queue} for each
  * priority level.
  */
class QueueArrayPriorityQueue[E](val priorityLevels: Int)
  extends PriorityQueue[E] {

  // Fill in the methods and fields.  The signature of copy() method
  // should have QueueArrayPriorityQueue[E] as the return type, not
  // just PriorityQueue[E] .
  private var queueArrayPriorityQueue = new Array[collection.mutable.Queue[E]](priorityLevels)

  /**
    * If the priority is less then zero or greater than priorityLevel
    * return a IllegalArgumentException
    *
    * If the queueArrayPriorityQueue at specific index (priority)
    * is null, than create a new mutable Queue and add the element to the
    * Queue.
    *
    * If the queue does exist than append the element to the immutable array.
    *
    * @param element
    * @param priority
    */
  def push(element: E, priority: Int): Unit = {

    if (priority < 0 || priority > this.priorityLevels) {
      throw new IllegalArgumentException
    }

    if (queueArrayPriorityQueue(priority) == null) {
      queueArrayPriorityQueue(priority) = new Queue[E]()
      queueArrayPriorityQueue(priority) += element
    } else {
      queueArrayPriorityQueue(priority) += element
    }

  }

  /**
    * A for loop that will iterate through the immutable array and
    * checks if the list at the specific index is not null and nonEmpty;
    * if either of these are true, return false because it is not empty.
    *
    * If it iterates through the entire for loop without hitting the
    * if statement, return true;
    *
    * @return
    */
  def isEmpty: Boolean = {

    for (i <- 0 until priorityLevels) {
      if (queueArrayPriorityQueue(i) != null && queueArrayPriorityQueue(i).nonEmpty) {
        return false
      }
    }
    true
  }

  /**
    * A for loop, iterate through the queueArrayPriorityQueue it
    * iterates through the specific sized array
    * if the queueArrayPriorityQueue at the specific index has elements in the
    * mutable Queue and it is not null we keep track of the index with the
    * index variable.
    *
    * The boolean named break is used for exiting the if statement so you do not
    * overwrite the index variable.
    *
    * @return
    */
  def pop(): E = {

    var index: Int = 0
    var bool: Boolean = true
    for (i <- 0 until priorityLevels) {
      if (queueArrayPriorityQueue(i).nonEmpty && bool
        && queueArrayPriorityQueue(i) != null) {
        index = i
        bool = false
      }
    }
    queueArrayPriorityQueue(index).dequeue()
  }

  /*
  Method is used for debugging
   */
  def print(): Unit = {
    for (i <- 0 until priorityLevels) {
      println(queueArrayPriorityQueue(i))
    }
  }

  type Impl = QueueArrayPriorityQueue[E]

  /**
    * Will need a double for loop to interate through the immutable array
    * of queue. Get the element at the i for the queue by using the apply,
    * because this will return the element at that index, and use x as
    * the priroity level, which really is just the index of the immutable array
    *
    * @return
    */
  def copy(): Impl = {
    val queueArrayPriorityQueueCopy = new QueueArrayPriorityQueue[E](priorityLevels)
    for (x <- 0 until queueArrayPriorityQueue.size) {
      for (i <- 0 until queueArrayPriorityQueue(x).size) {
        queueArrayPriorityQueueCopy.push(queueArrayPriorityQueue(x).apply(i), x)
      }
    }
    queueArrayPriorityQueueCopy
  }
}

/** An implementation of {@link cs421scala.PriorityQueue PriorityQueue}
  * using a single instance of some Scala linear storage
  * (such as =ArrayList=) internally.
  */
class SingleQueuePriorityQueue[E](val priorityLevels: Int)
  extends PriorityQueue[E] {

  private var singleQueuePriorityQueue = new ArrayBuffer[Helper]()

  /** Helper class wrapping instances of `E` with a priority level
    */
  protected class Helper(val item: E, val priority: Int) {
    // You may add additional fields/methods here if it helps you,
    // or otherwise leave it blank/delete the body

    // used to see what each item and priority of Helper are
    override def toString(): String = {
      item + " " + priority
    }
  }

  /**
  * If the priority is less than zero or greater than the priorityLevels
  * than throw new Illegal Argument Exception
  *
  * Create a new Helper object than append to the end of the ArrayBuffer
  */
  def push(element: E, priority: Int): Unit = {

    if (priority < 0 || priority > this.priorityLevels) {
      throw new IllegalArgumentException
    }

    val helper = new Helper(element, priority)
    singleQueuePriorityQueue.append(helper)
  }

  /**
  * if the ArrayBuffer isEmpty it will be false,
  * If the ArrayBuffer is nonEmpty it will be true
  */
  def isEmpty: Boolean = {
    singleQueuePriorityQueue.isEmpty
  }

  /**
    * If the singleQueuePriorityQueue isEmpty it will
    *  throw a NoSuchElementException
    *
    *  I will search through the arraylist for the lowest priority level
    *  and I will swap the current lowestPriority for the priority of the
    *  helper
    *
    *  After finding the lowest priority, I will do another forloop
    *  to get the index of the first helper object that has that priority number
    *
    *  Once i get this index, I will remove the helper from the ArrayBuffer and
    *  than return the item in the removed Helper object
  */
  def pop(): E = {

    if ( singleQueuePriorityQueue.isEmpty ) {
      throw new NoSuchElementException("Queue does not contain any elements")
    }

    var lowestPriority: Int = priorityLevels
    var index:Int = 0
    for (h <- singleQueuePriorityQueue) {
      if ( h.priority < lowestPriority) {
        lowestPriority = h.priority
      }
    }

    for( h <-singleQueuePriorityQueue ) {
      if( h.priority == lowestPriority ) {
        index = singleQueuePriorityQueue.indexOf(h)
      }
    }

    val returnedHelper = singleQueuePriorityQueue.remove(index)
    returnedHelper.item
  }

  /**
    * Method for debugging
    */
  def print(): Unit = {
    for (i <- 0 until singleQueuePriorityQueue.size) {
      println(singleQueuePriorityQueue(i).toString())
    }
  }

  override type Impl = SingleQueuePriorityQueue[E]

  /**
    * Returns a shallow copy of the singleQueuePriorityQueue
    *
    * The for loop iterates through the mutable ArrayBuffer
    * and pushes the x.item and x.priority into new singleQueuePriorityQueue
    * @return
    */
  def copy(): Impl = {
    val singleQueuePriorityQueueCopy = new SingleQueuePriorityQueue[E](priorityLevels)
    for (x <- singleQueuePriorityQueue) {
      singleQueuePriorityQueueCopy.push(x.item, x.priority)
    }
    singleQueuePriorityQueueCopy
  }

}
