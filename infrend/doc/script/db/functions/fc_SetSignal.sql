USE [infrend]
GO
/****** Object:  UserDefinedFunction [dbo].[fc_SetSignal]    Script Date: 03/08/2012 08:51:52 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[fc_SetSignal]
(
	-- Add the parameters for the function here
	@TEXT VARCHAR(10),
	@CASAS_DECIMAIS INT = 2
)
RETURNS NUMERIC(12,2)
AS
BEGIN
	DECLARE @RESULT NUMERIC,
			@DIGITO CHAR(1)

	SET @RESULT = CONVERT(NUMERIC, dbo.Translate(@TEXT, '{ABCDEFGHI}JKLMNOPQR', '01234567890123456789'))
	SET @DIGITO = SUBSTRING(@TEXT, LEN(@TEXT), 1)
	
	IF(@DIGITO = '}' OR @DIGITO >= 'J' AND @DIGITO <= 'R')
		SET @RESULT = @RESULT*-1
	
	RETURN @RESULT/POWER(10, @CASAS_DECIMAIS)
	
END
