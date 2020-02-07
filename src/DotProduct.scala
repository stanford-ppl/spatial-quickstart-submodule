import spatial.dsl._
import spatial.lib.ML._

@spatial class DotProduct extends SpatialTest {

  val N = 1024
  val op = 1
  val ts = 32
  val ip = 16

  type X = Int

  def dotproduct[T:Num](aIn: Array[T], bIn: Array[T]) = {

    val a = DRAM[T](N)
    val b = DRAM[T](N)
    val out = ArgOut[T]
    setMem(a, aIn)
    setMem(b, bIn)

    Accel {
      val accO = Reg[T](0.to[T])
      out := Reduce(accO)(N by ts par op){i =>
        val aBlk = SRAM[T](ts)
        val bBlk = SRAM[T](ts)
        Parallel {
          aBlk load a(i::i+ts par ip)
          bBlk load b(i::i+ts par ip)
        }
        dp_flat(ts, ip) { ii => (aBlk(ii), bBlk(ii)) }
      }{_+_}
    }
    out
  }

  def main(args: Array[String]): Unit = {
    val a = Array.tabulate(N){ i => i.to[X] }
    val b = Array.tabulate(N){ i => i.to[X] }

    val out = dotproduct[X](a, b)
    val gold = a.zip(b){_*_}.reduce{_+_}

    val cksum = checkGold(out, gold)
    println("PASS: " + cksum + " (DotProduct)")
    assert(cksum)
  }
}
