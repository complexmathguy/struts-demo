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

 	$( document ).on('refreshGameGrid', function() {
        populateGameGrid();
    });

	$( document ).ready(function()
	{
		$('#selectForGame').DualListBox();
		
		var rowNum, height;
		var caption = '';
		var modelUrl = '<%=request.getParameter( "modelUrl" )%>';
		
		if (  modelUrl == 'null' )
		{
			caption 	= 'Game List';
			modelUrl 	= '${pageContext.request.contextPath}/Game/viewAll';
			rowNum		= 20;
			height		= 280; 
		}
		else
		{
			rowNum		= 5;
			height		= 100;
			$('#dualListButtonForGameDivId').show(); 
		}
			
        var grid = jQuery("#GameGridTableId");
        grid.jqGrid({
        	url: modelUrl,
			datatype: "json",
													 			    						colNames:[ 'Game Id','Frames', ],
   			colModel:[ {name:'gameId',index:'gameId',hidden:true},{name:'frames',index:'frames',align:'center'}, ],
   			loadtext: "Loading Game List",
			altRows:true,
			sortable:true,
			autowidth:true,
			shrinkToFit : true,
			multiselect:true,
			pager:'#GamePager',
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

	jQuery('#GameGridTableId').dblclick(function () 
	{
		editGame();
	}); 			    

	function addGame()
	{
		var title = "Add New Game";
		var url	= '<%=request.getParameter( "addUrl" )%>';		
		var eventToFire = "refreshGameGrid";
		
		if( url == 'null' )
			url = "${pageContext.request.contextPath}/jsp/GameProfileForm.jsp?action=create"
			
		inspectionDialog( title, url, eventToFire );
	}

	function editGame()
	{
		var id 	= getSelectedIdFromGrid( jQuery('#GameGridTableId'), 'gameId' );
	
		if ( id != null )
		{
			var title = "Edit Game";
			var url = '${pageContext.request.contextPath}/jsp/GameProfileForm.jsp?action=edit&game.gameId=' + id;
			
			var eventToFire = "refreshGameGrid";
			
			inspectionDialog( title, url, eventToFire );
		}
		else
		{
			BootstrapDialog.alert('Please first select a Game');
		}	
	}
	
	function deleteGame()
	{
		var ids 	= getSelectedIdsFromGrid( jQuery('#GameGridTableId'), 'gameId' );
		if ( ids == null )
		{
			BootstrapDialog.alert('Please first select a Game');
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
	                		url = '${pageContext.request.contextPath}/Game/delete?a=a';
	                	
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
							populateGameGrid()
						});		
	                }
	            }
	    	});
	    }
	}

	function addGameFromAllList()
	{
		var sourceUrl	= '${pageContext.request.contextPath}/Game/viewAll';
		var modelUrl 	= '<%=request.getParameter( "modelUrl" )%>';
		var value 		= 'gameId';
		var text 		= 'frames';
		var roleName	= '<%=request.getParameter( "roleName" )%>';
		
		if ( roleName == 'null' )
			roleName = 'All-Game';
			
		multiselect( sourceUrl, modelUrl, roleName, value, text, assignGameSelectionsFromAllList );  
	}

	function assignGameSelectionsFromAllList( ids )
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
			populateGameGrid()
		});		
	}
	
	function populateGameGrid()
	{
		$('#loadingDivId').show();
		$('#GameGridTableId').trigger( 'reloadGrid' );
		$('#loadingDivId').hide();			
	}
	
</script>

<table id="GameGridTableId"></table>
<div id="GamePager" style="font-size:0.9em"></div>
<div>
<br>
<table
  <tr>
    <td>
		<a href="#" data-toggle="tooltip" data-placement="below" title="refresh" onclick="populateGameGrid()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-refresh">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="add Game" onclick="addGame()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-plus">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="edit Game" onclick="editGame()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-pencil">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="delete Game" onclick="deleteGame()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-trash">
		      </span>
			</button>
		</a>
	  </td>
	  <td>
		<div id="dualListButtonForGameDivId" style="display:none">
		  <a href="#" data-toggle="tooltip" data-placement="below" title="add Game From List" onclick="addGameFromAllList()">
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
  loading all Game entities...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
</div>				  				  

