function checkFrom(idList)
{
	if(idList != null)
	{
		// span.selected-id, a tag span eh criada a partir da adicao do atributo class (selected-id) em h:outputText
		$('div.ui-datatable div.ui-dt-c span.selected-id').each(function(index)
		{
			var parentTr = $(this).closest('tr')
			var span = parentTr.find('span.ui-chkbox-icon')
			var checkbox = parentTr.find('input[type="checkbox"]')
			if($.inArray(Number($(this).text()), idList) >= 0)
			{
				// atraves da adicao da class 'ui-icon ui-icon-check' que o span.ui-chkbox-icon eh checked
				span.addClass('ui-icon ui-icon-check');							
				checkbox.attr('checked', true);
				// eh necessario tornar a div pai da tag span ativa
				span.parent('div').addClass('ui-state-active')
			}
			else
			{
				// atraves da remocao da class 'ui-icon ui-icon-check' que o span.ui-chkbox-icon eh unchecked
				span.removeClass('ui-icon ui-icon-check');
				checkbox.attr('checked', false);
				// eh necessario tornar a div pai da tag span inativa
				span.parent('div').removeClass('ui-state-active')
			}
		})
	}
}