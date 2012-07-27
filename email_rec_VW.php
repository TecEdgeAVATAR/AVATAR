<?php

/* connect to gmail */
$hostname = '{imap.gmail.com:993/imap/ssl}INBOX';
$username = 'sate2012.avatar@gmail.com';
$password = 'EmbraceChaos';

/* try to connect */
$inbox = imap_open($hostname,$username,$password) or die('Cannot connect to Gmail: ' . imap_last_error());

/* grab emails */
imap_sort( $inbox, SORTDATE, 0);
$emails = imap_search($inbox,'UNDELETED');

/* if emails are returned, cycle through each... */
if($emails) {
  
	/* begin output var */
	$output = '<br>\n\n</br>';

	/* put the newest emails on top */
	rsort($emails);
  
  
	//$fp = fopen('email.txt','w' );
	//if (!$fp) {$fp = null;
	//echo '<br>\n</br>ERROR: Unable to open file.</table></body></html>'; }

	/* for every email... */
	foreach($emails as $email_number) {
    
	
	
	
		//if (!$fp) {echo 'ERROR: Unable to open file.</table></body></html>'; exit;}
		
		/* get information specific to this email */
		$overview = imap_fetch_overview($inbox,$email_number,0);
		$message = imap_fetchbody($inbox,$email_number,1);
    
	
	
		if ( strpos( ''.$overview[0]->subject, 'POINT:') !== false )
		{	//if $overview contains "POINT:" 
		
			/* output the email header information */
			$output.= "<br>@@Subject: " . $overview[0]->subject;
			$output.= "<br>@@From: " . $overview[0]->from;
			$output.= "<br>@@Date: " . $overview[0]->date;
			
			echo "\n@@@". $overview[0]->subject."\n";
			//echo "<br>\n</br>From: " . $overview[0]->from;
			//echo "<br>\n</br>Date: " . $overview[0]->date;
			
			/* output the email body */
			//$output.= "\n@@Message: " . $message;
			$output.= "<br>\n</br>";
		}
	}
	/*if ($fp != null)
	{
		fwrite($fp, $output);
		fwrite($fp, "\n");
		fclose($fp);
	}*/
	echo "\n\n@@@END OF MESSAGES<br>\n\n</br>";
	//echo $output."\n\n";
} 

/* close the connection */
imap_close($inbox);

?>
