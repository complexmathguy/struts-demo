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

 	$( document ).on('refreshMatchupGrid', function() {
        populateMatchupGrid();
    });

	$( document ).ready(function()
	{
		$('#selectForMatchup').DualListBox();
		
		var rowNum, height;
		var caption = '';
		var modelUrl = '<%=request.getParameter( "modelUrl" )%>';
		
		if (  modelUrl == 'null' )
		{
			caption 	= 'Matchup List';
			modelUrl 	= '${pageContext.request.contextPath}/Matchup/viewAll';
			rowNum		= 20;
			height		= 280; 
		}
		else
		{
			rowNum		= 5;
			height		= 100;
			$('#dualListButtonForMatchupDivId').show(); 
		}
			
        var grid = jQuery("#MatchupGridTableId");
        grid.jqGrid({
        	url: modelUrl,
			datatype: "json",
													 			    						colNames:[ 'Matchup Id','Name', ],
   			colModel:[ {name:'matchupId',index:'matchupId',hidden:true},{name:'name',index:'name',align:'center'}, ],
   			loadtext: "Loading Matchup List",
			altRows:true,
			sortable:true,
			autowidth:true,
			shrinkToFit : true,
			multiselect:true,
			pager:'#MatchupPager',
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

	jQuery('#MatchupGridTableId').dblclick(function () 
	{
		editMatchup();
	}); 			    

	function addMatchup()
	{
		var title = "Add New Matchup";
		var url	= '<%=request.getParameter( "addUrl" )%>';		
		var eventToFire = "refreshMatchupGrid";
		
		if( url == 'null' )
			url = "${pageContext.request.contextPath}/jsp/MatchupProfileForm.jsp?action=create"
			
		inspectionDialog( title, url, eventToFire );
	}

	function editMatchup()
	{
		var id 	= getSelectedIdFromGrid( jQuery('#MatchupGridTableId'), 'matchupId' );
	
		if ( id != null )
		{
			var title = "Edit Matchup";
			var url = '${pageContext.request.contextPath}/jsp/MatchupProfileForm.jsp?action=edit&matchup.matchupId=' + id;
			
			var eventToFire = "refreshMatchupGrid";
			
			inspectionDialog( title, url, eventToFire );
		}
		else
		{
			BootstrapDialog.alert('Please first select a Matchup');
		}	
	}
	
	function deleteMatchup()
	{
		var ids 	= getSelectedIdsFromGrid( jQuery('#MatchupGridTableId'), 'matchupId' );
		if ( ids == null )
		{
			BootstrapDialog.alert('Please first select a Matchup');
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
	                		url = '${pageContext.request.contextPath}/Matchup/delete?a=a';
	                	
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
							populateMatchupGrid()
						});		
	                }
	            }
	    	});
	    }
	}

	function addMatchupFromAllList()
	{
		var sourceUrl	= '${pageContext.request.contextPath}/Matchup/viewAll';
		var modelUrl 	= '<%=request.getParameter( "modelUrl" )%>';
		var value 		= 'matchupId';
		var text 		= 'name';
		var roleName	= '<%=request.getParameter( "roleName" )%>';
		
		if ( roleName == 'null' )
			roleName = 'All-Matchup';
			
		multiselect( sourceUrl, modelUrl, roleName, value, text, assignMatchupSelectionsFromAllList );  
	}

	function assignMatchupSelectionsFromAllList( ids )
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
			populateMatchupGrid()
		});		
	}
	
	function populateMatchupGrid()
	{
		$('#loadingDivId').show();
		$('#MatchupGridTableId').trigger( 'reloadGrid' );
		$('#loadingDivId').hide();			
	}
	
</script>

<table id="MatchupGridTableId"></table>
<div id="MatchupPager" style="font-size:0.9em"></div>
<div>
<br>
<table
  <tr>
    <td>
		<a href="#" data-toggle="tooltip" data-placement="below" title="refresh" onclick="populateMatchupGrid()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-refresh">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="add Matchup" onclick="addMatchup()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-plus">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="edit Matchup" onclick="editMatchup()" >
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-pencil">
		      </span>
			</button>
		</a>
		<a href="#" data-toggle="tooltip" data-placement="below" title="delete Matchup" onclick="deleteMatchup()">
		    <button type="button" class="btn btn-outline-primary">
		      <span class="glyphicon glyphicon-trash">
		      </span>
			</button>
		</a>
	  </td>
	  <td>
		<div id="dualListButtonForMatchupDivId" style="display:none">
		  <a href="#" data-toggle="tooltip" data-placement="below" title="add Matchup From List" onclick="addMatchupFromAllList()">
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
  loading all Matchup entities...<image src="${pageContext.request.contextPath}/img/load_new.gif" width=48 height=48/>
</div>				  				  

