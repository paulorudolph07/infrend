USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[usp_ProcessaRegistroArquivo]    Script Date: 02/08/2013 09:56:30 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[usp_ProcessaRegistroArquivo] (
	@ID_SPEC_ARQUIVO BIGINT,
    @ANO INT,
	@MES_FIM INT = 0
)

AS

BEGIN
 /**************************************************************************   
  * FUNÇÃO: LOOP NOS REGISTROS DA TABELA REGISTRO_ARQUIVO PARA CRIACAO DE  *
  * OCORRENCIAS                                                            *
  *------------------------------------------------------------------------*
  * CRIADA EM    : 17/02/2012 - PAULO RUDOLPH                              *
  *------------------------------------------------------------------------*
  * ÚLTIMAS ATUALIZAÇÕES:                                                  *
  * - 31/03/2011 - PAULO RUDOLPH                                           *
  *              - REVISÃO DE LIMPEZA DE VARIÁVEIS.                        *
  **************************************************************************/
  
  DECLARE 	@REGISTRO VARCHAR(8000),
			@ID_CLIENTE BIGINT,
			@ID_TIPO_CLIENTE BIGINT,
            @ID_OPERACAO BIGINT,
			@OPERACAO_VALUE VARCHAR(2),
			@ID_DADO_BANCARIO BIGINT,

			@OcorrenciaTransaction VARCHAR(22)

	-- INICIALIZA AS VARIÁVEIS
    SET @REGISTRO = NULL
	-- ///

	-- MONTA O CURSOR DE REGISTROS DA TABELA REGISTRO_ARQUIVO
	DECLARE rCURSOR CURSOR LOCAL FOR
		SELECT R.TX_REGISTRO
			FROM REGISTRO_ARQUIVO R
	-- ///

	-- ABRE O CURSOR
	OPEN rCURSOR

	-- BUSCA AS INFORMAÇÕES DO PRIMEIRO REGISTRO
	FETCH NEXT FROM rCURSOR INTO @REGISTRO
	
	-- LOOP NOS REGISTROS SELECIONADOS
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		EXEC usp_InsertCliente @ID_SPEC_ARQUIVO, @ANO, @REGISTRO, @ID_CLIENTE OUT, @ID_TIPO_CLIENTE OUT
		IF @ID_CLIENTE IS NULL
			GOTO NEXT_REG

		EXEC usp_InsertDadoBancario @ID_SPEC_ARQUIVO, @REGISTRO, @ID_CLIENTE, @ID_DADO_BANCARIO OUT
		IF @ID_DADO_BANCARIO IS NULL
			GOTO NEXT_REG

		--SET @ID_OPERACAO = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'operacao', @ID_SPEC_ARQUIVO))
		exec usp_GetPropertyValue 'operacao', @REGISTRO, @ID_SPEC_ARQUIVO, @OPERACAO_VALUE OUT
		SET @ID_OPERACAO = CONVERT(BIGINT, @OPERACAO_VALUE)

		BEGIN TRAN @OcorrenciaTransaction
		BEGIN TRY
			-- insere ocorrencia para pf
			IF @ID_TIPO_CLIENTE = 1
			BEGIN
				EXEC usp_InsertOcorrenciaPf @ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO,
											@ANO = @ANO,
											@ID_OPERACAO = @ID_OPERACAO,
											@ID_DADO_BANCARIO = @ID_DADO_BANCARIO,
											@REGISTRO = @REGISTRO
			END -- END IF
			-- insere ocorrencia para pj
			ELSE IF @ID_TIPO_CLIENTE = 2
			BEGIN -- BEGIN ELSE IF
				EXEC usp_InsertOcorrenciaPj @ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO,
											@ANO = @ANO,
											@MES_FIM = @MES_FIM,
											@ID_OPERACAO = @ID_OPERACAO,
											@ID_DADO_BANCARIO = @ID_DADO_BANCARIO,
											@REGISTRO = @REGISTRO
			
			END -- END ELSE IF
			COMMIT TRAN @OcorrenciaTransaction 
		END TRY
		BEGIN CATCH
			ROLLBACK TRAN @OcorrenciaTransaction
			BEGIN TRANSACTION
				INSERT INTO LG_PROCESSAMENTO VALUES (
					GETDATE(), 
					ERROR_NUMBER(), 
					ERROR_SEVERITY(), 
					ERROR_STATE(), 
					ERROR_PROCEDURE(), 
					ERROR_LINE(), 
					ERROR_MESSAGE(), 
					'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + '; ANO: ' + CONVERT(VARCHAR, @ANO) + ';ULTIMO MES: ' + CONVERT(VARCHAR, @MES_FIM) + '; ID_CLIENTE: ' + CONVERT(VARCHAR, @ID_CLIENTE),
					@REGISTRO
				 )
			COMMIT
		END CATCH

		NEXT_REG:
		-- BUSCA AS INFORMAÇÕES DO PRÓXIMO REGISTRO
		FETCH NEXT FROM rCURSOR INTO @REGISTRO
	END -- (FIM DO LOOP NOS REGISTROS SELECIONADOS) ///

	-- FECHA E DESTROI O CURSOR
	CLOSE rCURSOR
	DEALLOCATE rCURSOR
	--///
END
