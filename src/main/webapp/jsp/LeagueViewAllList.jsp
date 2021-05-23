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

 	$( document ).on('refreshLeagueGrid', function() {
        populateLeagueGrid();
    });

	$( document ).ready(function()
	{
		$('#selectForLeague').DualListBox();
		
		var rowNum, height;
		var caption = '';
		var modelUrl = '<%=request.getParameter( "modelUrl" )%>';
		
		if (  modelUrl == 'null' )
		{
			caption 	= 'League List';
			modelUrl 	= '${pageContext.request.contextPath}/League/viewAll';
			rowNum		= 20;
			height		= 280; 
		}
		else
		{
			rowNum		= 5;
			height		= 100;
			$('#dualListButtonForLeagueDivId').show(); 
		}
			
        var grid = jQuery("#LeagueGridTableId");
        grid.jqGrid({
        	url: modelUrl,
			datatype: "json",
													 			    						colNames:[ 'League Id','Name', ],
   			colModel:[ {name:'leagueId',index:'leagueId',hidden:true},{name:'name',index:'name',align:'center'}, ],
   			loadtext: "Loading League List",
			altRows:true,
			sortable:true,
			autowidth:true,
			shrinkToFit : true,
			multiselect:true,
			pager:'#LeaguePager',
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

	jQuery('#LeagueGridTableId').dblclick(function () 
	{
		editLeague();
	}); 			    

	function addLeague()
	{
		var title = "Add New League";
		var url	= '<%=request.getParameter( "addUrl" )%>';		
		var eventToFire = "refreshLeagueGrid";
		
		if( url == 'null' )
			url = "${pageContext.request.contextPath}/jsp/LeagueProfileForm.jsp?action=create"
			
		inspectionDialog( title, url, eventToFire );
	}

	function editLeague()
	{
		var id 	= getSelectedIdFromGrid( jQuery('#LeagueGridTableId'), 'leagueId' );
	
		if ( id != null )
		{
			var title = "Edit League";
			var url = '${pageContext.request.contextPath}/jsp/LeagueProfileForm.jsp?action=edit&league.leagueId=' + id;
			
			var eventToFire = "refreshLeagueGrid";
			
			inspectionDialog( title, url, eventToFire );
		}
		else
		{
			BootstrapDialog.alert('Please first select a League');
		}	
	}
	
	function deleteLeague()
	{
		var ids 	= getSelectedIdsFromGrid( jQuery('#LeagueGridTableId'), 'leagueId' );
		if ( ids == null )
		{
			BootstrapDialog.alert('Please first select a League');
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
	                		url = '${pageContext.request.contextPath}/League/delete?a=a';
	                	
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
							populateLeagueGrid()
						});		
	                }
	            }
	    	});
	    }
	}

	function addLeagueFromAllList()
	{
		var sourceUrl	= '${pageContext.request.contextPath}/League/viewAll';
		var modelUrl 	= '<%=request.getParameter( "modelUrl" )%>';
		var value 		= 'leagueId';
		var text 		= 'name';
		var roleName	= '<%=request.getParameter( "roleName" )%>';
		
		if ( roleName == 'null' )
			roleName = 'All-League';
			
		multiselect( sourceUrl, modelUrl, roleName, value, text, assignLeagueSelectionsFromAllList );  
	}

	function assignLeagueSelectionsFromAllList( ids )
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
			populateLeagueGrid()
		});		
	}
	
	function populateLeagueGrid()
	{
		$('#loadingDivId').show();
		$('#LeagueGridTableId').trigger( 'reloadGrid' );
		$('#loadingDivId').hide();			
	}
	
</script>

<table id="LeagueGridTableId"></table>
<div id="LeaguePager" style="font-size:0.9em"></div>
<div>
<br>
<table
  <tr>
    <td>
		<a href="#" data-toggle="tooltip" data-placement="below" title="refresh" onclick="populateLeagueGrid()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-refresh">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="add League" onclick="addLeague()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-plus">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="edit League" onclick="editLeague()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-pencil">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="delete League" onclick="deleteLeague()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-trash">
		      </span>
			</button>
		</a>
	  </td>
	  <td>
		<div id="dualListButtonForLeagueDivId" style="display:none">
		  <a href="#" data-toggle="tooltip" data-placement="below" title="add League From List" onclick="addLeagueFromAllList()">
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
  loading all League entities...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
</div>				  				  

