package org.apache.spark.mllib.knn

import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partition, TaskContext}

class KNNRDD[T](val rootTree: Tree[T],
                @transient childrenTree: RDD[Tree[T]]) extends RDD[(Vector, T)](childrenTree) {

  @DeveloperApi
  override def compute(split: Partition, context: TaskContext): Iterator[(Vector, T)] =
    firstParent[Tree[T]].iterator(split, context).flatMap(_.iterator)

  override protected def getPartitions: Array[Partition] = firstParent.partitions

  override def getPreferredLocations(split: Partition): Seq[String] =
    firstParent.preferredLocations(split)
}
