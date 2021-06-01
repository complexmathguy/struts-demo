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
			$( '#TournamentAssociationDivId' ).show();
	 
	 	var id = '<%=request.getParameter("tournament.tournamentId")%>';

		// if edit, populate form fields		
		if ( action == 'edit' )
		{
			var id = '<%=request.getParameter("tournament.tournamentId")%>';
			
			jQuery.ajax(
			{
		  		url: '${pageContext.request.contextPath}/Tournament/load?tournament.tournamentId=' + id,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var allControls = document.querySelectorAll("input, select, textArea");

				for(var i=0; i < allControls.length; i++)
				{
					var element = allControls[i];
					var elementName = element.name.replace( "tournament.", "" );
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
	
	function doneSavingTournament()
	{
		document.getElementById("TournamentFormStatusDivId").innerHTML = '<span style="color:blue">done saving</span>';
	}
	
	function resetTournament()
	{
		document.getElementById("TournamentProfileFormId").reset();
	}
	
	function saveTournament()
	{
		$('#TournamentFormLoadingDivId').show();

		// need to force the val to be handled by the Boolean type
		$("#TournamentProfileFormId input:checkbox").each(function()
		{
    		if ( $(this).is(':checked') == false ) 
    			$(this).val( 'false' );    			
    		else
    			$(this).val('true');
  		});

		var url 		= '${pageContext.request.contextPath}/Tournament/save';		
		var parentUrl 	= '<%=request.getParameter("parentUrl")%>';
		var formData 	= $('#TournamentProfileFormId').serialize();
		var action 		= '<%=request.getParameter("action")%>';
		
		if ( action == 'create' ) 
			url = '${pageContext.request.contextPath}/Tournament/create';
			
		jQuery.ajax(
		{
	  		url: url,
	  		dataType: 'json',
	  		data : formData,
		}).always(function( data ) 
		{
			if ( parentUrl != 'null' )
			{
				parentUrl = parentUrl + '&childId=' + data['tournamentId'];
				jQuery.ajax(
				{
			  		url: parentUrl,
			  		dataType: 'json',
				}).always(function( data ) 
				{
				});
			}

			if ( action == 'create' )
				$( '#TournamentAssociationDivId' ).show();

		    $('#TournamentFormLoadingDivId').hide();			
				
			doneSavingTournament();
		});
		
	
	}
		function addMatchups()
	{
		addHelperForTournament( 'Matchup', 'Matchups' );
	}

	function deleteMatchups()
	{
		var ids;
		deleteHelperForTournament( 'Matchups', 'name', ids );
	}
	
	function addMatchupsFromList()
	{
	}
	
        function addHelperForTournament( associationClassType, roleName )
	{    
		var title = "Add New " + associationClassType + " for Tournament " + roleName;
		var parentId = document.getElementById("tournament.tournamentId").value;
		parentUrl = '${pageContext.request.contextPath}/Tournament/save' + roleName + '?tournament.tournamentId=' + parentId;
		var url = '${pageContext.request.contextPath}/jsp/' + associationClassType + 'ProfileForm.jsp?action=create&parentUrl=' + parentUrl + '&parentName=tournament&roleName=' + roleName;
		var eventToFire = 'refresh' + roleName + 'ForTournament';
		inspectionDialog( title, url, eventToFire );
    }

	function deleteHelperForTournament( nameOfRole, keyFieldName, ids )
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
				var elementName = "tournament." + nameOfRole.toLowerCase() + "." + keyFieldName;
				var element 	= document.getElementsByName(elementName)[0];
				var url 		= '${pageContext.request.contextPath}/Tournament/delete' + nameOfRole + '';
				
				url = url + '?childIds=' + ids;
				
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'text',
			  		data : $('#TournamentProfileFormId').serialize(),
				}).always(function( data ) 
				{
					document.getElementById("TournamentFormStatusDivId").innerHTML = '<span style="color:blue">done deleting</span>';
					element.value = ""; // clear it
				});
			}
    	});
	
	}
</script>

<div id="TournamentProfileFormDiv" style="padding:4px;border:2px solid lightgray">
  <form id="TournamentProfileFormId" class="formTableClass">
    <input type=hidden id="tournament.tournamentId" name="tournament.tournamentId" />  		  	
<!-- Direct Attributes -->          	
<table class="formTableClass">
	<tr class="formTRClass">
	  <td class="formTDClass">name</td>
	  <td class="formTDClass">	<input type='text' id="tournament.name" name="tournament.name" placeHolder="name" required="" validate="" class="form-control" />
	
	</td>
	</tr>
	<tr class="formTRClass">
	  <td class="formTDClass">Type</td>
	  <td class="formTDClass">		
	<select id="tournament.type" name="tournament.type" class="form-control selectizing_class fieldSelectClass" style="display:none">
	    	    	<option value='1'>Pro</option>
	    	    	<option value='2'>Amateur</option>
	    	</select>
	</td>
	</tr>
</table>

<!-- Composites -->          	
    <table class="formTableClass">
    </table>
  	<br>
	<div>
		<a href="#" data-toggle="tooltip" data-placement="below" title="save Tournament" onclick="saveTournament()">
		    <button type="button" class="btn btn-outline-primary">
		      	Save
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="reset" onclick="resetTournament()">
		    <button type="button" class="btn btn-outline-primary">
		      	Reset
			</button>
		</a>
		<div id="TournamentFormLoadingDivId" style="display:none;color:black">
  			saving Tournament...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
		</div>				  				  
		<div id="TournamentFormStatusDivId">
		</div>	
	</div> 
  </form>
</div>

<div id="TournamentAssociationDivId" style="display:none">
  <div class="singleAssociationClass">
    <table class="associationTableClass">
	</table>
  </div>

  <div "class=multipleAssociationClass">  
	<table class="associationTableClass">  
		  <tr class="formRowClass">
	    <td class="formTDClass">
		  <div class="sectionClass">
		    <div class="sectionHeaderClass">
		      <span class="sectionTitleClass">
		        Matchups
		      </span>
		    </div>
		    <div class="sectionBodyClass">	  
	          <table class="formTableClass">
	            <tr class="formTRClass">
	              <td class="formTDClass">
<%
  parentId	= "tournament.tournamentId=" + request.getParameter("tournament.tournamentId");
  parentUrl	= "/Tournament/saveMatchups?" + parentId;
  addUrl 	= "/jsp/MatchupProfileForm.jsp?action=create&parentUrl=" + parentUrl;
  deleteUrl = "/Tournament/deleteMatchups?" + parentId;
  modelUrl 	= "/Tournament/loadMatchups?" + parentId;
%>	              
	                <jsp:include page="MatchupViewAllList.jsp">
	                	<jsp:param name="roleName" value="Matchups"/>
	                	<jsp:param name="addUrl" value="<%=addUrl%>"/>
	                	<jsp:param name="deleteUrl" value="<%=deleteUrl%>"/>
	                	<jsp:param name="modelUrl" value="<%=modelUrl%>"/>
	                	<jsp:param name="parentUrl" value="<%=parentUrl%>"/>
	                </jsp:include>
	      	      </td>
	            </tr>
		      </table>
		    </div>
		  </div>
	    </td>
	  </tr>
		</table>

  </div>
</div>
			  				  


