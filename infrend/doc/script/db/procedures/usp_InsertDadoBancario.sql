-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE usp_InsertDadoBancario
	@ID_SPEC_ARQUIVO BIGINT,
	@REGISTRO VARCHAR(8000),
	@ID_CLIENTE BIGINT,
	@ID_DADO_BANCARIO BIGINT OUT
AS
BEGIN
	DECLARE @NU_AGENCIA VARCHAR(3),
			@NU_CONTA VARCHAR(7)

	--SET @NU_AGENCIA = dbo.fc_SubStringIn(@REGISTRO, 'agencia', @ID_SPEC_ARQUIVO)
	--SET @NU_CONTA = dbo.fc_SubStringIn(@REGISTRO, 'conta', @ID_SPEC_ARQUIVO)
	exec usp_GetPropertyValue 'agencia', @REGISTRO, @ID_SPEC_ARQUIVO, @NU_AGENCIA OUT
	exec usp_GetPropertyValue 'conta', @REGISTRO, @ID_SPEC_ARQUIVO, @NU_CONTA OUT

	SET @ID_DADO_BANCARIO = (SELECT ID_DADO_BANCARIO FROM DADO_BANCARIO 
					WHERE NU_AGENCIA = @NU_AGENCIA AND NU_CONTA = @NU_CONTA)

	IF @ID_DADO_BANCARIO IS NULL
	BEGIN
		INSERT INTO DADO_BANCARIO VALUES (@NU_AGENCIA, @NU_CONTA, @ID_CLIENTE)
		
		IF @@ERROR = 0
			SET @ID_DADO_BANCARIO = @@IDENTITY
		ELSE
		BEGIN
			INSERT INTO LG_PROCESSAMENTO VALUES (
				GETDATE(), 
				ERROR_NUMBER(), 
				ERROR_SEVERITY(), 
				ERROR_STATE(), 
				ERROR_PROCEDURE(), 
				ERROR_LINE(), 
				ERROR_MESSAGE(), 
				'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + 
				'; ID_CLIENTE: ' + CONVERT(VARCHAR, @ID_CLIENTE) + 
				'; AGENCIA: ' + @NU_AGENCIA + 
				'; CONTA: ' + @NU_CONTA,
				@REGISTRO
			 )
		END
	END
END
GO
