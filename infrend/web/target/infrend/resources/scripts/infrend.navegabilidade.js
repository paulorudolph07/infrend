(function($)
{
	$(document).ready(function()
	{
		$('div.ui-tabs-panels > div[id^="tab-view:"]').prepend($('#navegabilidade').css('display', ''));
	});
})(jQuery);