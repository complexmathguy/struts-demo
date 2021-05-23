<%
  String parentId	= null;
  String parentUrl	= null;
  String addUrl 	= null;
  String deleteUrl 	= null;
  String modelUrl 	= null;
%>	              
<link href="${pageContext.request.contextPath}/css/toggle.checkbox.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/bootstrap-datetimepicker.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-mask-as-number.js"></script>

<style>
  @import url('http://code.ionicframework.com/ionicons/2.0.0/css/ionicons.min.css');

	* {
		margin: 0;
		padding: 0;
		box-sizing: border-box;
		font-family: 'Lato', sans-serif;
	}

</style>

<script>
    $('.maskedTextField').maskAsNumber();
</script>

<script type="text/javascript">
    $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:mm:ss',autoclose: true,todayBtn: true});
</script>             

<script type="text/javascript">

	$( document ).ready(function()
	{
		// initialize the dom selectizing select controls
		
		$( '.selectizing_class' ).each(function()
		{
			$(this).selectizing();
		});

		var action = '<%=request.getParameter("action")%>';

	 	if ( action == 'edit' )
			$( '#PlayerAssociationDivId' ).show();
	 
	 	var id = '<%=request.getParameter("player.playerId")%>';

		// if edit, populate form fields		
		if ( action == 'edit' )
		{
			var id = '<%=request.getParameter("player.playerId")%>';
			
			jQuery.ajax(
			{
		  		url: '${pageContext.request.contextPath}/Player/load?player.playerId=' + id,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var allControls = document.querySelectorAll("input, select, textArea");

				for(var i=0; i < allControls.length; i++)
				{
					var element = allControls[i];
					var elementName = element.name.replace( "player.", "" );
					var val;
					
					var indexes = elementName.split( "." );
					if ( indexes.length == 1 )
						val = data[indexes[0]];
					else if ( indexes.length == 2 )
						val = data[indexes[0]][indexes[1]];

					if ( val !== undefined && element.type )
					{
						if ( element.type === 'checkbox' )
						{
							if ( val == true )
								element.checked = true;
							else
								element.checked = false;
						}
						else if ( element.type === 'select' )
						{
							// select the right option
						}
						element.value = val;
					}
				}
			});
		}
	});
	
	function doneSavingPlayer()
	{
		document.getElementById("PlayerFormStatusDivId").innerHTML = '<span style="color:blue">done saving</span>';
	}
	
	function resetPlayer()
	{
		document.getElementById("PlayerProfileFormId").reset();
	}
	
	function savePlayer()
	{
		$('#PlayerFormLoadingDivId').show();

		// need to force the val to be handled by the Boolean type
		$("#PlayerProfileFormId input:checkbox").each(function()
		{
    		if ( $(this).is(':checked') == false ) 
    			$(this).val( 'false' );    			
    		else
    			$(this).val('true');
  		});

		var url 		= '${pageContext.request.contextPath}/Player/save';		
		var parentUrl 	= '<%=request.getParameter("parentUrl")%>';
		var formData 	= $('#PlayerProfileFormId').serialize();
		var action 		= '<%=request.getParameter("action")%>';
		
		if ( action == 'create' ) 
			url = '${pageContext.request.contextPath}/Player/create';
			
		jQuery.ajax(
		{
	  		url: url,
	  		dataType: 'json',
	  		data : formData,
		}).always(function( data ) 
		{
			if ( parentUrl != 'null' )
			{
				parentUrl = parentUrl + '&childId=' + data['playerId'];
				jQuery.ajax(
				{
			  		url: parentUrl,
			  		dataType: 'json',
				}).always(function( data ) 
				{
				});
			}

			if ( action == 'create' )
				$( '#PlayerAssociationDivId' ).show();

		    $('#PlayerFormLoadingDivId').hide();			
				
			doneSavingPlayer();
		});
		
	
	}
    function addHelperForPlayer( associationClassType, roleName )
	{    
		var title = "Add New " + associationClassType + " for Player " + roleName;
		var parentId = document.getElementById("player.playerId").value;
		parentUrl = '${pageContext.request.contextPath}/Player/save' + roleName + '?player.playerId=' + parentId;
		var url = '${pageContext.request.contextPath}/jsp/' + associationClassType + 'ProfileForm.jsp?action=create&parentUrl=' + parentUrl + '&parentName=player&roleName=' + roleName;
		var eventToFire = 'refresh' + roleName + 'ForPlayer';
		inspectionDialog( title, url, eventToFire );
    }

	function deleteHelperForPlayer( nameOfRole, keyFieldName, ids )
	{
    	BootstrapDialog.confirm
    	({
        	title: 'WARNING',
        	message: 'Are you sure?',
            type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
            closable: true, // <-- Default value is false
            btnCancelLabel: 'No', // <-- Default value is 'Cancel',
            btnOKLabel: 'Yes', // <-- Default value is 'OK',
            btnOKClass: 'btn-warning', // <-- If you didn't specify it, dialog type will be used,
            callback: function(result) 
            {
				var elementName = "player." + nameOfRole.toLowerCase() + "." + keyFieldName;
				var element 	= document.getElementsByName(elementName)[0];
				var url 		= '${pageContext.request.contextPath}/Player/delete' + nameOfRole + '';
				
				url = url + '?childIds=' + ids;
				
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'text',
			  		data : $('#PlayerProfileFormId').serialize(),
				}).always(function( data ) 
				{
					document.getElementById("PlayerFormStatusDivId").innerHTML = '<span style="color:blue">done deleting</span>';
					element.value = ""; // clear it
				});
			}
    	});
	
	}
</script>

<div id="PlayerProfileFormDiv" style="padding:4px;border:2px solid lightgray">
  <form id="PlayerProfileFormId" class="formTableClass">
    <input type=hidden id="player.playerId" name="player.playerId" />  		  	
<!-- Direct Attributes -->          	
<table class="formTableClass">
	<tr class="formTRClass">
	  <td class="formTDClass">name</td>
	  <td class="formTDClass">	<input type='text' id="player.name" name="player.name" placeHolder="name" required="" validate="" class="form-control" />
	
	</td>
	</tr>
	<tr class="formTRClass">
	  <td class="formTDClass">dateOfBirth</td>
	  <td class="formTDClass">	<input size="16" type="text" value="2012-06-15 14:45" readonly class="form_datetime form-control">
	</td>
	</tr>
	<tr class="formTRClass">
	  <td class="formTDClass">height</td>
	  <td class="formTDClass">	<input type='text' id="player.height" name="player.height" placeHolder="height" required="" validate="" class="form-control" />
	
	</td>
	</tr>
	<tr class="formTRClass">
	  <td class="formTDClass">isProfessional</td>
	  <td class="formTDClass">		<div class="checkbox checkbox-slider--a-rounded">
	  <label><input type='checkbox' id='player.isProfessional' name='player.isProfessional' class="form-control" />
	  		<span></span>
	  </label>	
	</div>
	</td>
	</tr>
</table>

<!-- Composites -->          	
    <table class="formTableClass">
    </table>
  	<br>
	<div>
		<a href="#" data-toggle="tooltip" data-placement="below" title="save Player" onclick="savePlayer()">
		    <button type="button" class="btn btn-outline-primary">
		      	Save
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="reset" onclick="resetPlayer()">
		    <button type="button" class="btn btn-outline-primary">
		      	Reset
			</button>
		</a>
		<div id="PlayerFormLoadingDivId" style="display:none;color:black">
  			saving Player...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
		</div>				  				  
		<div id="PlayerFormStatusDivId">
		</div>	
	</div> 
  </form>
</div>

<div id="PlayerAssociationDivId" style="display:none">
  <div class="singleAssociationClass">
    <table class="associationTableClass">
	</table>
  </div>

  <div "class=multipleAssociationClass">  
	<table class="associationTableClass">  
	</table>

  </div>
</div>
			  				  


