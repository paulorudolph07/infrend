USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[usp_FileReader]    Script Date: 02/08/2013 09:53:00 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[usp_FileReader] 
	-- Add the parameters for the stored procedure here
	@FILEPATH VARCHAR(255)
AS
BEGIN
	DECLARE @sql NVARCHAR(4000)
	SET @sql = 'BULK INSERT dbo.REGISTRO_ARQUIVO FROM ''' + @FILEPATH + ''' WITH ( ROWTERMINATOR =''\n'' )';	
	EXEC(@sql);
END
