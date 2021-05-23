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

 	$( document ).on('refreshTournamentGrid', function() {
        populateTournamentGrid();
    });

	$( document ).ready(function()
	{
		$('#selectForTournament').DualListBox();
		
		var rowNum, height;
		var caption = '';
		var modelUrl = '<%=request.getParameter( "modelUrl" )%>';
		
		if (  modelUrl == 'null' )
		{
			caption 	= 'Tournament List';
			modelUrl 	= '${pageContext.request.contextPath}/Tournament/viewAll';
			rowNum		= 20;
			height		= 280; 
		}
		else
		{
			rowNum		= 5;
			height		= 100;
			$('#dualListButtonForTournamentDivId').show(); 
		}
			
        var grid = jQuery("#TournamentGridTableId");
        grid.jqGrid({
        	url: modelUrl,
			datatype: "json",
													 			    							 			    						colNames:[ 'Tournament Id','Name','Type', ],
   			colModel:[ {name:'tournamentId',index:'tournamentId',hidden:true},{name:'name',index:'name',align:'center'},{name:'type',index:'type',align:'center'}, ],
   			loadtext: "Loading Tournament List",
			altRows:true,
			sortable:true,
			autowidth:true,
			shrinkToFit : true,
			multiselect:true,
			pager:'#TournamentPager',
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

	jQuery('#TournamentGridTableId').dblclick(function () 
	{
		editTournament();
	}); 			    

	function addTournament()
	{
		var title = "Add New Tournament";
		var url	= '<%=request.getParameter( "addUrl" )%>';		
		var eventToFire = "refreshTournamentGrid";
		
		if( url == 'null' )
			url = "${pageContext.request.contextPath}/jsp/TournamentProfileForm.jsp?action=create"
			
		inspectionDialog( title, url, eventToFire );
	}

	function editTournament()
	{
		var id 	= getSelectedIdFromGrid( jQuery('#TournamentGridTableId'), 'tournamentId' );
	
		if ( id != null )
		{
			var title = "Edit Tournament";
			var url = '${pageContext.request.contextPath}/jsp/TournamentProfileForm.jsp?action=edit&tournament.tournamentId=' + id;
			
			var eventToFire = "refreshTournamentGrid";
			
			inspectionDialog( title, url, eventToFire );
		}
		else
		{
			BootstrapDialog.alert('Please first select a Tournament');
		}	
	}
	
	function deleteTournament()
	{
		var ids 	= getSelectedIdsFromGrid( jQuery('#TournamentGridTableId'), 'tournamentId' );
		if ( ids == null )
		{
			BootstrapDialog.alert('Please first select a Tournament');
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
	                		url = '${pageContext.request.contextPath}/Tournament/delete?a=a';
	                	
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
							populateTournamentGrid()
						});		
	                }
	            }
	    	});
	    }
	}

	function addTournamentFromAllList()
	{
		var sourceUrl	= '${pageContext.request.contextPath}/Tournament/viewAll';
		var modelUrl 	= '<%=request.getParameter( "modelUrl" )%>';
		var value 		= 'tournamentId';
		var text 		= 'name';
		var roleName	= '<%=request.getParameter( "roleName" )%>';
		
		if ( roleName == 'null' )
			roleName = 'All-Tournament';
			
		multiselect( sourceUrl, modelUrl, roleName, value, text, assignTournamentSelectionsFromAllList );  
	}

	function assignTournamentSelectionsFromAllList( ids )
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
			populateTournamentGrid()
		});		
	}
	
	function populateTournamentGrid()
	{
		$('#loadingDivId').show();
		$('#TournamentGridTableId').trigger( 'reloadGrid' );
		$('#loadingDivId').hide();			
	}
	
</script>

<table id="TournamentGridTableId"></table>
<div id="TournamentPager" style="font-size:0.9em"></div>
<div>
<br>
<table
  <tr>
    <td>
		<a href="#" data-toggle="tooltip" data-placement="below" title="refresh" onclick="populateTournamentGrid()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-refresh">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="add Tournament" onclick="addTournament()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-plus">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="edit Tournament" onclick="editTournament()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-pencil">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="delete Tournament" onclick="deleteTournament()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-trash">
		      </span>
			</button>
		</a>
	  </td>
	  <td>
		<div id="dualListButtonForTournamentDivId" style="display:none">
		  <a href="#" data-toggle="tooltip" data-placement="below" title="add Tournament From List" onclick="addTournamentFromAllList()">
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
  loading all Tournament entities...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
</div>				  				  

