USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[sp_DeleteRegistroArquivo]    Script Date: 12/21/2012 13:38:09 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[sp_DeleteRegistroArquivo] 
	-- Add the parameters for the stored procedure here
AS
BEGIN
	DELETE FROM REGISTRO_ARQUIVO
END
