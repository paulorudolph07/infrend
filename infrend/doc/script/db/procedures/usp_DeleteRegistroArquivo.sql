USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[usp_DeleteRegistroArquivo]    Script Date: 02/08/2013 09:51:52 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[usp_DeleteRegistroArquivo] 
	-- Add the parameters for the stored procedure here
AS
BEGIN
	DELETE FROM REGISTRO_ARQUIVO
END
