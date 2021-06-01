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
			$( '#GameAssociationDivId' ).show();
	 
	 	var id = '<%=request.getParameter("game.gameId")%>';
		
		$( "#multiSelectForMatchup" ).on("change", function(e)  
		{
			//assign the selected value
			var input = document.getElementById("MatchupInputId");				
			input.value = this.options[this.selectedIndex].text;
	
			if ( input.value != 'no selection' )
			{
				// save the Matchup
				var parentId = document.getElementById( 'game.gameId' ).value;
		 		var childId = this.options[this.selectedIndex].value;
		 		var url 	= '${pageContext.request.contextPath}/Game/saveMatchup.?game.gameId=' + parentId + "&childId=" + childId;
	
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'json',
				}).always(function( data ) 
				{
					doneSavingGame();
				});
			}
			// hide the select
			$("#multiSelectForMatchupDivId").hide();
		});
	
	 	$( document ).on('refreshMatchupForGame', function() 
	 	{			
	 		// reload Game and apply the Matchup
	 		var url 	= '${pageContext.request.contextPath}/Game/load?game.gameId=' + id;
	 		var element = document.getElementById( 'MatchupInputId' );
	 		var field 	= 'matchup.name';
	 		
			jQuery.ajax(
			{
		  		url: url,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var val;
				var indexes = field.split( "." );
				
				if ( indexes.length == 1 )
					val = data[indexes[0]];
				else if ( indexes.length == 2 )
					val = data[indexes[0]][indexes[1]];

		 		element.value = val;
			});
	    });
		
		$( "#multiSelectForPlayer" ).on("change", function(e)  
		{
			//assign the selected value
			var input = document.getElementById("PlayerInputId");				
			input.value = this.options[this.selectedIndex].text;
	
			if ( input.value != 'no selection' )
			{
				// save the Player
				var parentId = document.getElementById( 'game.gameId' ).value;
		 		var childId = this.options[this.selectedIndex].value;
		 		var url 	= '${pageContext.request.contextPath}/Game/savePlayer.?game.gameId=' + parentId + "&childId=" + childId;
	
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'json',
				}).always(function( data ) 
				{
					doneSavingGame();
				});
			}
			// hide the select
			$("#multiSelectForPlayerDivId").hide();
		});
	
	 	$( document ).on('refreshPlayerForGame', function() 
	 	{			
	 		// reload Game and apply the Player
	 		var url 	= '${pageContext.request.contextPath}/Game/load?game.gameId=' + id;
	 		var element = document.getElementById( 'PlayerInputId' );
	 		var field 	= 'player.name';
	 		
			jQuery.ajax(
			{
		  		url: url,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var val;
				var indexes = field.split( "." );
				
				if ( indexes.length == 1 )
					val = data[indexes[0]];
				else if ( indexes.length == 2 )
					val = data[indexes[0]][indexes[1]];

		 		element.value = val;
			});
	    });

		// if edit, populate form fields		
		if ( action == 'edit' )
		{
			var id = '<%=request.getParameter("game.gameId")%>';
			
			jQuery.ajax(
			{
		  		url: '${pageContext.request.contextPath}/Game/load?game.gameId=' + id,
		  		dataType: 'json',
			}).always(function( data ) 
			{
				var allControls = document.querySelectorAll("input, select, textArea");

				for(var i=0; i < allControls.length; i++)
				{
					var element = allControls[i];
					var elementName = element.name.replace( "game.", "" );
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
	
	function doneSavingGame()
	{
		document.getElementById("GameFormStatusDivId").innerHTML = '<span style="color:blue">done saving</span>';
	}
	
	function resetGame()
	{
		document.getElementById("GameProfileFormId").reset();
	}
	
	function saveGame()
	{
		$('#GameFormLoadingDivId').show();

		// need to force the val to be handled by the Boolean type
		$("#GameProfileFormId input:checkbox").each(function()
		{
    		if ( $(this).is(':checked') == false ) 
    			$(this).val( 'false' );    			
    		else
    			$(this).val('true');
  		});

		var url 		= '${pageContext.request.contextPath}/Game/save';		
		var parentUrl 	= '<%=request.getParameter("parentUrl")%>';
		var formData 	= $('#GameProfileFormId').serialize();
		var action 		= '<%=request.getParameter("action")%>';
		
		if ( action == 'create' ) 
			url = '${pageContext.request.contextPath}/Game/create';
			
		jQuery.ajax(
		{
	  		url: url,
	  		dataType: 'json',
	  		data : formData,
		}).always(function( data ) 
		{
			if ( parentUrl != 'null' )
			{
				parentUrl = parentUrl + '&childId=' + data['gameId'];
				jQuery.ajax(
				{
			  		url: parentUrl,
			  		dataType: 'json',
				}).always(function( data ) 
				{
				});
			}

			if ( action == 'create' )
				$( '#GameAssociationDivId' ).show();

		    $('#GameFormLoadingDivId').hide();			
				
			doneSavingGame();
		});
		
	
	}
		function addMatchup()
	{
		addHelperForGame( 'Matchup', 'Matchup' );
	}
	
	function deleteMatchup()
	{ 
		deleteHelperForGame( 'Matchup', 'name' );
	}

	function addMatchupFromList()
	{
		// display the multi-select (viewAll and Matchup lists)
		$("#multiSelectForMatchupDivId").show();

		jQuery.ajax(
		{
		    url: "${pageContext.request.contextPath}/Matchup/viewAll",
		    dataType: 'json',
		}).always(function( data ) 
		{
			$("#multiSelectForMatchup").empty();
			
			var selectId	= "multiSelectForMatchup";
			var text 		= 'name';
			var value 		= 'matchupId';
			var includeBlank = true;
			
			loadOptionsWithJSONData( selectId, data, value, text, includeBlank  );
		});
	}
		
    		function addPlayer()
	{
		addHelperForGame( 'Player', 'Player' );
	}
	
	function deletePlayer()
	{ 
		deleteHelperForGame( 'Player', 'name' );
	}

	function addPlayerFromList()
	{
		// display the multi-select (viewAll and Player lists)
		$("#multiSelectForPlayerDivId").show();

		jQuery.ajax(
		{
		    url: "${pageContext.request.contextPath}/Player/viewAll",
		    dataType: 'json',
		}).always(function( data ) 
		{
			$("#multiSelectForPlayer").empty();
			
			var selectId	= "multiSelectForPlayer";
			var text 		= 'name';
			var value 		= 'playerId';
			var includeBlank = true;
			
			loadOptionsWithJSONData( selectId, data, value, text, includeBlank  );
		});
	}
		
        function addHelperForGame( associationClassType, roleName )
	{    
		var title = "Add New " + associationClassType + " for Game " + roleName;
		var parentId = document.getElementById("game.gameId").value;
		parentUrl = '${pageContext.request.contextPath}/Game/save' + roleName + '?game.gameId=' + parentId;
		var url = '${pageContext.request.contextPath}/jsp/' + associationClassType + 'ProfileForm.jsp?action=create&parentUrl=' + parentUrl + '&parentName=game&roleName=' + roleName;
		var eventToFire = 'refresh' + roleName + 'ForGame';
		inspectionDialog( title, url, eventToFire );
    }

	function deleteHelperForGame( nameOfRole, keyFieldName, ids )
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
				var elementName = "game." + nameOfRole.toLowerCase() + "." + keyFieldName;
				var element 	= document.getElementsByName(elementName)[0];
				var url 		= '${pageContext.request.contextPath}/Game/delete' + nameOfRole + '';
				
				url = url + '?childIds=' + ids;
				
				jQuery.ajax(
				{
			  		url: url,
			  		dataType: 'text',
			  		data : $('#GameProfileFormId').serialize(),
				}).always(function( data ) 
				{
					document.getElementById("GameFormStatusDivId").innerHTML = '<span style="color:blue">done deleting</span>';
					element.value = ""; // clear it
				});
			}
    	});
	
	}
</script>

<div id="GameProfileFormDiv" style="padding:4px;border:2px solid lightgray">
  <form id="GameProfileFormId" class="formTableClass">
    <input type=hidden id="game.gameId" name="game.gameId" />  		  	
<!-- Direct Attributes -->          	
<table class="formTableClass">
	<tr class="formTRClass">
	  <td class="formTDClass">frames</td>
	  <td class="formTDClass">	<input type='text' id="game.frames" name="game.frames" placeHolder="frames" required="" validate="" class="maskedTextField form-control" />

	</td>
	</tr>
</table>

<!-- Composites -->          	
    <table class="formTableClass">
    </table>
  	<br>
	<div>
		<a href="#" data-toggle="tooltip" data-placement="below" title="save Game" onclick="saveGame()">
		    <button type="button" class="btn btn-outline-primary">
		      	Save
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="reset" onclick="resetGame()">
		    <button type="button" class="btn btn-outline-primary">
		      	Reset
			</button>
		</a>
		<div id="GameFormLoadingDivId" style="display:none;color:black">
  			saving Game...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
		</div>				  				  
		<div id="GameFormStatusDivId">
		</div>	
	</div> 
  </form>
</div>

<div id="GameAssociationDivId" style="display:none">
  <div class="singleAssociationClass">
    <table class="associationTableClass">
		  <tr class="formTRClass">
	    <td class="formTDClass">
		  <div class="sectionClass">
		    <div class="sectionHeaderClass">
		      <span class="sectionTitleClass">
		        Matchup
		      </span>
		    </div>
		    <div class="sectionBodyClass">	  
	          <table class="formTableClass">
	            <tr class="formTRClass">
	              <td class="formTDClass">
	      		    <input type='text' id="MatchupInputId" name="game.matchup.name" class="form-control" disabled/>
	      	      </td>
	              <td  class="formTDClass" style="vertical-align:center">
	                <div id="multiSelectForMatchupDivId" style="display:none">
	                  <select id="multiSelectForMatchup" />
	                  <a href="#" onclick="$('#multiSelectForMatchupDivId').hide();">
				        <button type="button" class="btn btn-outline-primary">
				          <span class="glyphicon glyphicon-remove"/>
				 	    </button>
				      </a>
	                </div>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="create Matchup" onclick="addMatchup()">
  				      <button type="button" class="btn btn-outline-primary">
				        <span class="glyphicon glyphicon-plus"/>
					  </button>
				    </a>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="add Matchup from list" onclick="addMatchupFromList()">
				      <button type="button" class="btn btn-outline-primary">
				        <span class="glyphicon glyphicon-th-list"/>
					  </button>
				    </a>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="delete Matchup" onclick="deleteMatchup()">
				      <button type="button" class="btn btn-default btn-sm">
				        <span class="glyphicon glyphicon-trash"/>
				 	  </button>
				    </a>
	              </td>
	            </tr>
		      </table>
		    </div>
		  </div>
	    </td>
	  </tr>
			  <tr class="formTRClass">
	    <td class="formTDClass">
		  <div class="sectionClass">
		    <div class="sectionHeaderClass">
		      <span class="sectionTitleClass">
		        Player
		      </span>
		    </div>
		    <div class="sectionBodyClass">	  
	          <table class="formTableClass">
	            <tr class="formTRClass">
	              <td class="formTDClass">
	      		    <input type='text' id="PlayerInputId" name="game.player.name" class="form-control" disabled/>
	      	      </td>
	              <td  class="formTDClass" style="vertical-align:center">
	                <div id="multiSelectForPlayerDivId" style="display:none">
	                  <select id="multiSelectForPlayer" />
	                  <a href="#" onclick="$('#multiSelectForPlayerDivId').hide();">
				        <button type="button" class="btn btn-outline-primary">
				          <span class="glyphicon glyphicon-remove"/>
				 	    </button>
				      </a>
	                </div>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="create Player" onclick="addPlayer()">
  				      <button type="button" class="btn btn-outline-primary">
				        <span class="glyphicon glyphicon-plus"/>
					  </button>
				    </a>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="add Player from list" onclick="addPlayerFromList()">
				      <button type="button" class="btn btn-outline-primary">
				        <span class="glyphicon glyphicon-th-list"/>
					  </button>
				    </a>
				    <a href="#" data-toggle="tooltip" data-placement="below" title="delete Player" onclick="deletePlayer()">
				      <button type="button" class="btn btn-default btn-sm">
				        <span class="glyphicon glyphicon-trash"/>
				 	  </button>
				    </a>
	              </td>
	            </tr>
		      </table>
		    </div>
		  </div>
	    </td>
	  </tr>
		</table>
  </div>

  <div "class=multipleAssociationClass">  
	<table class="associationTableClass">  
	</table>

  </div>
</div>
			  				  


