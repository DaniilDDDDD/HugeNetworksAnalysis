package graphs

object TaskBRunner extends App {
  val name = GraphNames.Google
  val graph = parseGraph(name)
  println(s"Current graph is '$name'")
  println(s"Nodes in largest WCC: ${TaskB.maxWeakComponentPercent(graph)}")

  println("Kill x% of vertices with most degree: ")
  for(x <- 1 to 99) {
    graph.killMostDeg(x)
    val percent = TaskB.maxWeakComponentPercent(graph)
    println(s"$x\t$percent")
    graph.makeVerticesAlive
  }

  val attempts = 20
  println(s"Kill x% of random vertices (avg. of $attempts attempts): ")
  for(x <- 1 to 99) {
    var total = 0D
    for(_ <- 1 to attempts) {
      graph.killRandom(x)
      val percent = TaskB.maxWeakComponentPercent(graph)
      total += percent
      graph.makeVerticesAlive
    }
    println(s"$x\t${total / attempts}")
  }
}
