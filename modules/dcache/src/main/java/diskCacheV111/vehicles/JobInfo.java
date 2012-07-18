// $Id: JobInfo.java,v 1.3 2004-11-05 12:07:19 tigran Exp $

package diskCacheV111.vehicles;
import java.text.SimpleDateFormat ;
import java.util.Date;
import diskCacheV111.util.* ;

public class JobInfo implements java.io.Serializable {

   private final String _client;
   private final long   _clientId;
   private final long   _submitTime;
   private final long   _startTime;
   private final String _status;
   private final long   _jobId;

   public JobInfo( JobScheduler.Job job ){
       this(job, "<unknown>", 0);
   }

   public JobInfo( JobScheduler.Job job, String clientName , long clientId ){
       this(job.getSubmitTime(), job.getStartTime(), job.getStatusString(), job.getJobId(), clientName, clientId);
   }

   public JobInfo(long submitTime, long startTime, String status, int id, String clientName , long clientId ){
      _submitTime = submitTime ;
      _startTime  = startTime;
      _status     = status;
      _jobId      = id;
      _client   = clientName ;
      _clientId = clientId ;
   }
   public String getClientName(){ return _client ; }
   public long   getClientId(){ return _clientId ; }
   public long   getStartTime(){ return _startTime ; }
   public long   getSubmitTime(){ return _submitTime ; }
   public String getStatus(){ return _status  ;}
   public long   getJobId(){ return _jobId ; }
   private static SimpleDateFormat __format =
        new SimpleDateFormat( "MM/dd-HH:mm:ss" ) ;

   private static final long serialVersionUID = 5209798222006083955L;

   public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append(_jobId).append(";");
      sb.append(_client).append(":").append(_clientId) ;
      sb.append(";").append(__format.format(new Date(_startTime))).
         append(";").append(__format.format(new Date(_submitTime))).
         append(";").append(_status).append(";") ;
      return sb.toString();
   }
}
