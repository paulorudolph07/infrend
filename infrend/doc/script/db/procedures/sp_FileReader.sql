USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[sp_FileReader]    Script Date: 12/21/2012 13:37:55 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[sp_FileReader] 
	-- Add the parameters for the stored procedure here
	@FILEPATH VARCHAR(255)
AS
BEGIN
	DECLARE @sql NVARCHAR(4000)
	SET @sql = 'BULK INSERT dbo.REGISTRO_ARQUIVO FROM ''' + @FILEPATH + ''' WITH ( ROWTERMINATOR =''\n'' )';	
	EXEC(@sql);

	/*BULK INSERT dbo.REGISTRO_ARQUIVO
		FROM '@FILEPATH' WITH(ROWTERMINATOR = '\n');*/

END
