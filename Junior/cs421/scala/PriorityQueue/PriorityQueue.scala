package cs421scala

/** API for a priority queue
 * @tparam E The type of elements stored in this queue
 */
trait PriorityQueue[E] {

  /** Add an element to the queue at the given level of priority
   *
   * @throws {@link scala.IllegalArgumentException IllegalArgumentException}
   * when `priority` is less than zero or not less than `this.priorityLevels`.
   */
  def push(element:E, priority:Int):Unit

  /**
   * Returns `true` when the queue is empty.
   */
  def isEmpty:Boolean

  /**
   * Returns one element from the queue.  The value popped will have as high
   * or higher priority than every other element in the queue, and would have
   * been added before every other element of the queue with the same
   * priority.
   */
  def pop():E

  /**
   * Returns the number of priority levels availabel in this queue.  Priority
   * levels are numbered from 0 up.
   */
  def priorityLevels:Int

  /**
   * The actual implementation type of this PriorityQueue.
   */
  type Impl <: PriorityQueue[E]

  /**
   * Return a shallow copy of this this priority queue (that is, copying the
   * queue structure, but not the elements contained in the queue).
   */
  //def copy():Impl

}
