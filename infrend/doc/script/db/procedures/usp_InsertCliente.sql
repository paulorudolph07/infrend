USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[usp_InsertCliente]    Script Date: 02/08/2013 09:54:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[usp_InsertCliente] 
	@ID_SPEC_ARQUIVO BIGINT,
    @ANO INT,
	@REGISTRO VARCHAR(8000),
	@ID_CLIENTE BIGINT OUT,
	@ID_TIPO_CLIENTE BIGINT OUT
AS
BEGIN
	DECLARE @CD_CLIENTE VARCHAR(15)
	
	--SET @CD_CLIENTE = dbo.fc_SubStringIn(@REGISTRO, 'codigo', @ID_SPEC_ARQUIVO)
	exec usp_GetPropertyValue 'codigo', @REGISTRO, @ID_SPEC_ARQUIVO, @CD_CLIENTE OUT
	IF CONVERT(BIGINT, @CD_CLIENTE) = 0
	BEGIN -- IF
		INSERT INTO LG_PROCESSAMENTO VALUES (
				GETDATE(), 
				-9999, -- number 
				16, -- severity
				0, -- status
				OBJECT_NAME(@@PROCID), -- procedure
				59, -- linha
				'Erro no codigo do cliente', 
				'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + '; ANO: ' + CONVERT(VARCHAR, @ANO), 
				@REGISTRO
			)
	END -- IF
	ELSE
	BEGIN -- ELSE
		--SET @ID_TIPO_CLIENTE = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'tipoCliente', @ID_SPEC_ARQUIVO))
		exec usp_GetPropertyValue 'tipoCliente', @REGISTRO, @ID_SPEC_ARQUIVO, @ID_TIPO_CLIENTE OUT
		IF @ID_TIPO_CLIENTE IS NULL
			SET @ID_TIPO_CLIENTE = 2

		SET @ID_CLIENTE = (SELECT C.ID_CLIENTE FROM CLIENTE C 
								WHERE C.CD_CLIENTE = @CD_CLIENTE)
		BEGIN TRY
			IF @ID_CLIENTE IS NULL
			BEGIN -- BEGIN IF
				DECLARE @NOME VARCHAR(50),
						@LOGRADOURO VARCHAR(60),
						@BAIRRO VARCHAR(20),
						@CIDADE VARCHAR(20),
						@UF VARCHAR(2),
						@CEP VARCHAR(8)

				exec usp_GetPropertyValue 'nome', @REGISTRO, @ID_SPEC_ARQUIVO, @NOME OUT
				exec usp_GetPropertyValue 'logradouro', @REGISTRO, @ID_SPEC_ARQUIVO, @LOGRADOURO OUT
				exec usp_GetPropertyValue 'bairro', @REGISTRO, @ID_SPEC_ARQUIVO, @BAIRRO OUT
				exec usp_GetPropertyValue 'cidade', @REGISTRO, @ID_SPEC_ARQUIVO, @CIDADE OUT
				exec usp_GetPropertyValue 'uf', @REGISTRO, @ID_SPEC_ARQUIVO, @UF OUT
				exec usp_GetPropertyValue 'cep', @REGISTRO, @ID_SPEC_ARQUIVO, @CEP OUT

				INSERT INTO CLIENTE(
					CD_CLIENTE, 
					ID_TIPO_CLIENTE,
					NO_CLIENTE,
					NO_LOGRADOURO,
					NO_BAIRRO,
					NO_CIDADE,
					SG_ESTADO,
					CD_CEP
				) 
				VALUES (
					@CD_CLIENTE,
					@ID_TIPO_CLIENTE,
					@NOME,
					@LOGRADOURO,
					@BAIRRO,
					@CIDADE,
					@UF,
					@CEP
				)

				/*INSERT INTO CLIENTE(
					CD_CLIENTE, 
					ID_TIPO_CLIENTE,
					NO_CLIENTE,
					NO_LOGRADOURO,
					NO_BAIRRO,
					NO_CIDADE,
					SG_ESTADO,
					CD_CEP
				) 
				VALUES (
					@CD_CLIENTE,
					@ID_TIPO_CLIENTE,
					dbo.fc_SubStringIn(@REGISTRO, 'nome', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'logradouro', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'bairro', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'cidade', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'uf', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'cep', @ID_SPEC_ARQUIVO)
				)*/
				SET @ID_CLIENTE = @@IDENTITY
			END -- END IF
		END TRY
		BEGIN CATCH
			IF @ID_CLIENTE IS NULL
				SET @ID_CLIENTE = 0
			
			INSERT INTO LG_PROCESSAMENTO VALUES (
				GETDATE(), 
				ERROR_NUMBER(), 
				ERROR_SEVERITY(), 
				ERROR_STATE(), 
				ERROR_PROCEDURE(), 
				ERROR_LINE(), 
				ERROR_MESSAGE(), 
				'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + 
				'; ANO: ' + CONVERT(VARCHAR, @ANO) + 
				'; ID_CLIENTE: ' + CONVERT(VARCHAR, @ID_CLIENTE),
				@REGISTRO
			 )
			SET @ID_CLIENTE = NULL
		END CATCH
	END -- END ELSE
END
