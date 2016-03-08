<?php
require_once('include/config.php'); 
if (isset($_SESSION["UserName"]))
 {
require_once('include/connection.php');
include 'template/header.php';
		$showallenq = mysql_query("select e.eID,e.eRef,u.Uname,u.uCont,e.eSociety,e.eDoc,e.eTime,e.eMsg from enquiry e, userdetails u where  e.gID = '2' and e.uID = u.uID and e.uID = '".$_SESSION['uID']."'  ",$conn);
		}
	else
	{
		 echo "<script>window.location.href = 'login.php';</script>";
	}

 ?>
  <body class="hold-transition skin-blue sidebar-mini">
    <div class="wrapper">

      <?php 
	  include 'template/top-menu.php'; 
	  include 'template/sidebar.php'; 
		
		
	
	  ?>
      <!-- Left side column. contains the logo and sidebar -->
      <

      <!-- Content Wrapper. Contains page content -->
      <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
          <h1>
            View Enquiry
           <!-- <small>Preview</small>-->
          </h1>
          <ol class="breadcrumb">
            <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
           
            <li class="active">View Enquiry</li>
          </ol>
        </section>

        <!-- Main content -->
        <section class="content">
          <div class="row">
           <div class="col-md-12">
			<table class="table table-striped">
				<tr style="background:#ffffff;">
					<th>Reference No</th>
					<th>OBP Name</th>
					<!--<th>OBP Contact</th>-->
					<th>Society</th>
					<th>Slip</th>
					<th>Message</th>
					<th colspan="4">Date</th>
				</tr>
				<?php 	
					while($row = mysql_fetch_array($showallenq))
					{
					?>
				<tr>
					<td><?php echo $row[1]; ?></td>
					<td><?php echo $row[2]; ?></td>
					<!--<td><?php echo $row[3]; ?></td>-->
					<td><?php echo $row[4]; ?></td>
					<td><a href="admin/download.php?download_file=<?php echo$row[5]; ?>"><img style="width:100px;" src="<?php echo 'admin/Enquiry/'.$row[5]; ?>"</a></td>
					<td><?php echo $row[7]; ?></td>
					<td><?php echo $row[6]; ?></td>
					<td><a href="enquiry_details.php?ID=<?php echo $row[0]; ?>">View</a></td>
					
				</tr>
					<?php }?>
			</table>
		   </div>
          </div>   <!-- /.row -->
        </section><!-- /.content -->
      </div><!-- /.content-wrapper -->
      <?php include 'template/footer.php' ?>
