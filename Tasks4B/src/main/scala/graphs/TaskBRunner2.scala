package graphs

import java.io.{File, FileOutputStream}

object TaskBRunner2 extends App {
  def flushToFile(str: String, stream: FileOutputStream): Unit = {
    println(str)
    stream.write((str + "\n").getBytes)
    stream.flush()
  }

  def mainTask(name: String, outputMost: File, outputRand: File)(attempts: Int, mostStart: Int = 1, randStart: Int = 1): Unit = {
    val graph = parseGraph(name)

    val commonInfo =
      s"""Current graph is '$name'
        |Nodes in largest WCC: ${TaskB.maxWeakComponentPercent(graph)}
        |""".stripMargin

    val outputMostStream = new FileOutputStream(outputMost)
    flushToFile(commonInfo, outputMostStream)
    flushToFile("Kill x% of vertices with most degree: ", outputMostStream)
    for(x <- mostStart to 99) {
      graph.killMostDeg(x)
      val percent = TaskB.maxWeakComponentPercent(graph)
      flushToFile(s"$x\t$percent", outputMostStream)
      graph.makeVerticesAlive
    }

    outputMostStream.close()

    val outputRandStream = new FileOutputStream(outputRand)
    flushToFile(commonInfo, outputRandStream)
    flushToFile(s"Kill x% of random vertices (avg. of $attempts attempts): ", outputRandStream)
    for(x <- randStart to 99) {
      var total = 0D
      for(_ <- 1 to attempts) {
        graph.killRandom(x)
        val percent = TaskB.maxWeakComponentPercent(graph)
        total += percent
        graph.makeVerticesAlive
      }
      flushToFile(s"$x\t${total / attempts}", outputRandStream)
    }
    outputRandStream.close()
  }

  val root = "D:/System/Documents"
  mainTask(GraphNames.Google, new File(root, "google-most2.txt"), new File(root, "google-rand2.txt"))(10, 100, 61)
  mainTask(GraphNames.VK, new File(root, "vk-most.txt"), new File(root, "vk-rand.txt"))(1)
}
