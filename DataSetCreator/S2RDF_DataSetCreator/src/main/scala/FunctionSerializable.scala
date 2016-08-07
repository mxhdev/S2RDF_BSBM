/* 
* Object had to be created because of open Spark Issue https://issues.apache.org/jira/browse/SPARK-5594
* Function extractObj needs to be serializable!
 */

package dataCreator
import java.sql.Timestamp
import java.sql.Date
import java.text.SimpleDateFormat
import scala.util.matching.Regex
import org.apache.spark.sql.types._
import org.apache.spark.sql._
import scala.reflect.runtime.universe

  
object FunctionSerializable extends Serializable{

  def extractObj(r: Row, predType: String): Row = {

		var pattern = new Regex("(?<=\").*?(?=\")")
		var obj = r.getString(1)
		var result = obj
	
		if(obj.contains("^^")) {
			result = pattern.findFirstIn(obj).get
			if(predType.equals("timestamp")){
			  result = result.replace("T", " ")
			}
		}
		
		var result2 = Row(r.getString(0), result)
		
		if(predType.equals("int")){
		  val ret = result.toInt
		  result2 = Row(r.getString(0), ret)
		}else if(predType.equals("double")){
		  val ret = result.toDouble
		  result2 = Row(r.getString(0), ret)
		}else if(predType.equals("date")){
		  val ret = getDate(result)
		  result2 = Row(r.getString(0), ret)
		}else if(predType.equals("timestamp")){
		  val ret = getTimestamp(result)
		  result2 = Row(r.getString(0), ret)
		}
  
  	result2
	}
	
	def getDate(x:Any) :java.sql.Date = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    if (x.toString() == "") 
    return null
    else {
        val d = format.parse(x.toString());
        val t = new Date(d.getDate());
        return t
    }
  }
	
	def getTimestamp(x:Any) :java.sql.Timestamp = {
    val format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss")
    if (x.toString() == "") 
    return null
    else {
        val d = format.parse(x.toString());
        val t = new Timestamp(d.getTime());
        return t
    }
  }
}
