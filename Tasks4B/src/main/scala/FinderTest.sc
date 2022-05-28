import graphs._

import scala.collection.immutable.{TreeSet, HashMap}
import scala.collection.mutable

val a = new Node(1)
val b = new Node(2)
val c = new Node(3)
val d = new Node(4)
val e = new Node(5)
val f = new Node(6)
val g = new Node(7)

a addEdge b
c addEdge d
c addEdge e
e addEdge f
e addEdge f
d addEdge f
d addEdge e

val nodes = TreeSet(a,b,c,d,e,f,g)

TaskB.findWeakComponents(nodes)

Task4.findTriangles(nodes)

Task4.globalClusterCoeff(nodes)