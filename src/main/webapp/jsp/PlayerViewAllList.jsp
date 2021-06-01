<link href="http://code.jquery.com/ui/1.12.1/themes/cupertino/jquery-ui.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/ui.jqgrid.css" rel="stylesheet" type="text/css"/>

<style>
	.ui-jqgrid tr.jqgrow td,
	.ui-jqgrid th {
		font-size:0.8em
	}
</style> 

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jqGrid.min.js"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.jqGrid.src.js"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dual-list-box.min.js"/>
<script type="text/javascript" src="http://trirand.com/blog/jqgrid/js/i18n/grid.locale-en.js"></script>

<script type="text/javascript">

 	$( document ).on('refreshPlayerGrid', function() {
        populatePlayerGrid();
    });

	$( document ).ready(function()
	{
		$('#selectForPlayer').DualListBox();
		
		var rowNum, height;
		var caption = '';
		var modelUrl = '<%=request.getParameter( "modelUrl" )%>';
		
		if (  modelUrl == 'null' )
		{
			caption 	= 'Player List';
			modelUrl 	= '${pageContext.request.contextPath}/Player/viewAll';
			rowNum		= 20;
			height		= 280; 
		}
		else
		{
			rowNum		= 5;
			height		= 100;
			$('#dualListButtonForPlayerDivId').show(); 
		}
			
        var grid = jQuery("#PlayerGridTableId");
        grid.jqGrid({
        	url: modelUrl,
			datatype: "json",
													 			    							 			    							 			    							 			    						colNames:[ 'Player Id','Name','Date Of Birth','Height','Is Professional', ],
   			colModel:[ {name:'playerId',index:'playerId',hidden:true},{name:'name',index:'name',align:'center'},{name:'dateOfBirth',index:'dateOfBirth',align:'center'},{name:'height',index:'height',align:'center'},{name:'isProfessional',index:'isProfessional',align:'center'}, ],
   			loadtext: "Loading Player List",
			altRows:true,
			sortable:true,
			autowidth:true,
			shrinkToFit : true,
			multiselect:true,
			pager:'#PlayerPager',
		    rowNum:rowNum,
		    height:height,
		    rownumbers:true,
		    loadonce:false,
		    gridview:true,
		    jsonReader: {root:'rows',page:'page',total:'total',records:'records',repeatitems:false},
		    rowList: [5, 10, 20, 50],
		    viewrecords: true,
		    pgtext : "<span style='font-size: 0.8em'>Page {0} of {1}</span>",
		    recordtext: "<span style='font-size: 0.8em'>View {0} - {1} of {2}</span>",		    
		    caption: caption
		}); 
	});

	jQuery('#PlayerGridTableId').dblclick(function () 
	{
		editPlayer();
	}); 			    

	function addPlayer()
	{
		var title = "Add New Player";
		var url	= '<%=request.getParameter( "addUrl" )%>';		
		var eventToFire = "refreshPlayerGrid";
		
		if( url == 'null' )
			url = "${pageContext.request.contextPath}/jsp/PlayerProfileForm.jsp?action=create"
			
		inspectionDialog( title, url, eventToFire );
	}

	function editPlayer()
	{
		var id 	= getSelectedIdFromGrid( jQuery('#PlayerGridTableId'), 'playerId' );
	
		if ( id != null )
		{
			var title = "Edit Player";
			var url = '${pageContext.request.contextPath}/jsp/PlayerProfileForm.jsp?action=edit&player.playerId=' + id;
			
			var eventToFire = "refreshPlayerGrid";
			
			inspectionDialog( title, url, eventToFire );
		}
		else
		{
			BootstrapDialog.alert('Please first select a Player');
		}	
	}
	
	function deletePlayer()
	{
		var ids 	= getSelectedIdsFromGrid( jQuery('#PlayerGridTableId'), 'playerId' );
		if ( ids == null )
		{
			BootstrapDialog.alert('Please first select a Player');
		}
		else
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
	                // result will be true if button was click, while it will be false if 
	                // users close the dialog directly.
	                if(result) 
	                {
	                	var url = '<%=request.getParameter( "deleteUrl" )%>';
	                	
	                	if ( url == 'null' )
	                		url = '${pageContext.request.contextPath}/Player/delete?a=a';
	                	
	                	for (i = 0; i < ids.length; i++)
	                	{
	                		url = url + '&childIds=' + ids[i];
	                	}
	                							
						jQuery.ajax(
						{
					  		url: url,
					  		dataType: 'json',
						}).always(function( data ) 
						{
							populatePlayerGrid()
						});		
	                }
	            }
	    	});
	    }
	}

	function addPlayerFromAllList()
	{
		var sourceUrl	= '${pageContext.request.contextPath}/Player/viewAll';
		var modelUrl 	= '<%=request.getParameter( "modelUrl" )%>';
		var value 		= 'playerId';
		var text 		= 'name';
		var roleName	= '<%=request.getParameter( "roleName" )%>';
		
		if ( roleName == 'null' )
			roleName = 'All-Player';
			
		multiselect( sourceUrl, modelUrl, roleName, value, text, assignPlayerSelectionsFromAllList );  
	}

	function assignPlayerSelectionsFromAllList( ids )
	{
		var url = '<%=request.getParameter( "parentUrl" )%>';
		for (i = 0; i < ids.length; i++)
    	{
    		url = url + '&childIds=' + ids[i];
    	}
				
		jQuery.ajax(
		{
	  		url: url,
	  		dataType: 'json',
		}).always(function( data ) 
		{
			populatePlayerGrid()
		});		
	}
	
	function populatePlayerGrid()
	{
		$('#loadingDivId').show();
		$('#PlayerGridTableId').trigger( 'reloadGrid' );
		$('#loadingDivId').hide();			
	}
	
</script>

<table id="PlayerGridTableId"></table>
<div id="PlayerPager" style="font-size:0.9em"></div>
<div>
<br>
<table
  <tr>
    <td>
		<a href="#" data-toggle="tooltip" data-placement="below" title="refresh" onclick="populatePlayerGrid()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-refresh">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="add Player" onclick="addPlayer()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-plus">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="edit Player" onclick="editPlayer()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-pencil">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="delete Player" onclick="deletePlayer()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-trash">
		      </span>
			</button>
		</a>
	  </td>
	  <td>
		<div id="dualListButtonForPlayerDivId" style="display:none">
		  <a href="#" data-toggle="tooltip" data-placement="below" title="add Player From List" onclick="addPlayerFromAllList()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-th-list">
		      </span>
			</button>
		  </a>
		<div>
 	  </td>
 	</tr>
</table>

<div id="loadingDivId" style="display:none;color:black">
  loading all Player entities...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
</div>				  				  

