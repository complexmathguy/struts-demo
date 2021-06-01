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
			$( '#LeagueAssociationDivId' ).show();
	 
	 	var id = '<%=request.getParameter("league.leagueId")%>';

		// if edit, populate form fields		
		if ( action == 'edit' )
		{
			var id = '<%=request.getParameter("league.leagueId")%>';
			
			jQuery.ajax(
			{
		  		url: '${pageContext.request.contextPath}/League/load?league.leagueId=' + id,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var allControls = document.querySelectorAll("input, select, textArea");

				for(var i=0; i < allControls.length; i++)
				{
					var element = allControls[i];
					var elementName = element.name.replace( "league.", "" );
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
	
	function doneSavingLeague()
	{
		document.getElementById("LeagueFormStatusDivId").innerHTML = '<span style="color:blue">done saving</span>';
	}
	
	function resetLeague()
	{
		document.getElementById("LeagueProfileFormId").reset();
	}
	
	function saveLeague()
	{
		$('#LeagueFormLoadingDivId').show();

		// need to force the val to be handled by the Boolean type
		$("#LeagueProfileFormId input:checkbox").each(function()
		{
    		if ( $(this).is(':checked') == false ) 
    			$(this).val( 'false' );    			
    		else
    			$(this).val('true');
  		});

		var url 		= '${pageContext.request.contextPath}/League/save';		
		var parentUrl 	= '<%=request.getParameter("parentUrl")%>';
		var formData 	= $('#LeagueProfileFormId').serialize();
		var action 		= '<%=request.getParameter("action")%>';
		
		if ( action == 'create' ) 
			url = '${pageContext.request.contextPath}/League/create';
			
		jQuery.ajax(
		{
	  		url: url,
	  		dataType: 'json',
	  		data : formData,
		}).always(function( data ) 
		{
			if ( parentUrl != 'null' )
			{
				parentUrl = parentUrl + '&childId=' + data['leagueId'];
				jQuery.ajax(
				{
			  		url: parentUrl,
			  		dataType: 'json',
				}).always(function( data ) 
				{
				});
			}

			if ( action == 'create' )
				$( '#LeagueAssociationDivId' ).show();

		    $('#LeagueFormLoadingDivId').hide();			
				
			doneSavingLeague();
		});
		
	
	}
		function addPlayers()
	{
		addHelperForLeague( 'Player', 'Players' );
	}

	function deletePlayers()
	{
		var ids;
		deleteHelperForLeague( 'Players', 'name', ids );
	}
	
	function addPlayersFromList()
	{
	}
	
        function addHelperForLeague( associationClassType, roleName )
	{    
		var title = "Add New " + associationClassType + " for League " + roleName;
		var parentId = document.getElementById("league.leagueId").value;
		parentUrl = '${pageContext.request.contextPath}/League/save' + roleName + '?league.leagueId=' + parentId;
		var url = '${pageContext.request.contextPath}/jsp/' + associationClassType + 'ProfileForm.jsp?action=create&parentUrl=' + parentUrl + '&parentName=league&roleName=' + roleName;
		var eventToFire = 'refresh' + roleName + 'ForLeague';
		inspectionDialog( title, url, eventToFire );
    }

	function deleteHelperForLeague( nameOfRole, keyFieldName, ids )
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
				var elementName = "league." + nameOfRole.toLowerCase() + "." + keyFieldName;
				var element 	= document.getElementsByName(elementName)[0];
				var url 		= '${pageContext.request.contextPath}/League/delete' + nameOfRole + '';
				
				url = url + '?childIds=' + ids;
				
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'text',
			  		data : $('#LeagueProfileFormId').serialize(),
				}).always(function( data ) 
				{
					document.getElementById("LeagueFormStatusDivId").innerHTML = '<span style="color:blue">done deleting</span>';
					element.value = ""; // clear it
				});
			}
    	});
	
	}
</script>

<div id="LeagueProfileFormDiv" style="padding:4px;border:2px solid lightgray">
  <form id="LeagueProfileFormId" class="formTableClass">
    <input type=hidden id="league.leagueId" name="league.leagueId" />  		  	
<!-- Direct Attributes -->          	
<table class="formTableClass">
	<tr class="formTRClass">
	  <td class="formTDClass">name</td>
	  <td class="formTDClass">	<input type='text' id="league.name" name="league.name" placeHolder="name" required="" validate="" class="form-control" />
	
	</td>
	</tr>
</table>

<!-- Composites -->          	
    <table class="formTableClass">
    </table>
  	<br>
	<div>
		<a href="#" data-toggle="tooltip" data-placement="below" title="save League" onclick="saveLeague()">
		    <button type="button" class="btn btn-outline-primary">
		      	Save
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="reset" onclick="resetLeague()">
		    <button type="button" class="btn btn-outline-primary">
		      	Reset
			</button>
		</a>
		<div id="LeagueFormLoadingDivId" style="display:none;color:black">
  			saving League...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
		</div>				  				  
		<div id="LeagueFormStatusDivId">
		</div>	
	</div> 
  </form>
</div>

<div id="LeagueAssociationDivId" style="display:none">
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
		        Players
		      </span>
		    </div>
		    <div class="sectionBodyClass">	  
	          <table class="formTableClass">
	            <tr class="formTRClass">
	              <td class="formTDClass">
<%
  parentId	= "league.leagueId=" + request.getParameter("league.leagueId");
  parentUrl	= "/League/savePlayers?" + parentId;
  addUrl 	= "/jsp/PlayerProfileForm.jsp?action=create&parentUrl=" + parentUrl;
  deleteUrl = "/League/deletePlayers?" + parentId;
  modelUrl 	= "/League/loadPlayers?" + parentId;
%>	              
	                <jsp:include page="PlayerViewAllList.jsp">
	                	<jsp:param name="roleName" value="Players"/>
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
			  				  


