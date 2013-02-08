USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[sp_ProcessaRegistroArquivo]    Script Date: 12/28/2012 07:31:16 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[sp_ProcessaRegistroArquivo] (
	@ID_SPEC_ARQUIVO BIGINT,
    @ANO INT,
	@TRIMESTRE INT = 0
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
			@CD_CLIENTE VARCHAR(15),
			@NU_AGENCIA VARCHAR(3),
            @NU_CONTA VARCHAR(6),
            @ID_OPERACAO BIGINT,
			@ID_TIPO_CLIENTE BIGINT,

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
		SET @NU_AGENCIA = dbo.fc_SubStringIn(@REGISTRO, 'agencia', @ID_SPEC_ARQUIVO)
		SET @NU_CONTA = dbo.fc_SubStringIn(@REGISTRO, 'conta', @ID_SPEC_ARQUIVO)
		SET @CD_CLIENTE = dbo.fc_SubStringIn(@REGISTRO, 'codigo', @ID_SPEC_ARQUIVO)

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
					'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + '; ANO: ' + CONVERT(VARCHAR, @ANO) + ';AGENCIA: ' + @NU_AGENCIA + '; CONTA: ' + @NU_CONTA,
					@REGISTRO
				)
		END -- IF
		ELSE
		BEGIN -- ELSE
			SET @ID_TIPO_CLIENTE = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'tipoCliente', @ID_SPEC_ARQUIVO))
			IF @ID_TIPO_CLIENTE IS NULL
				SET @ID_TIPO_CLIENTE = 2

			SET @ID_CLIENTE = (SELECT C.ID_CLIENTE FROM CLIENTE C 
									WHERE C.CD_CLIENTE = @CD_CLIENTE AND C.NU_AGENCIA = @NU_AGENCIA AND C.NU_CONTA = @NU_CONTA)
			IF @ID_CLIENTE IS NULL
			BEGIN
				INSERT INTO CLIENTE(
					CD_CLIENTE, 
					ID_TIPO_CLIENTE,
					NO_CLIENTE,
					NO_LOGRADOURO,
					NO_BAIRRO,
					NO_CIDADE,
					SG_ESTADO,
					CD_CEP,
					NU_AGENCIA,
					NU_CONTA,
					NU_DIGITO
				) 
				VALUES (
					@CD_CLIENTE,
					@ID_TIPO_CLIENTE,
					dbo.fc_SubStringIn(@REGISTRO, 'nome', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'logradouro', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'bairro', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'cidade', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'uf', @ID_SPEC_ARQUIVO),
					dbo.fc_SubStringIn(@REGISTRO, 'cep', @ID_SPEC_ARQUIVO),
					@NU_AGENCIA,
					@NU_CONTA,
					dbo.fc_SubStringIn(@REGISTRO, 'digito', @ID_SPEC_ARQUIVO)
				)
				SET @ID_CLIENTE = @@IDENTITY
			END

			SET @ID_OPERACAO = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'operacao', @ID_SPEC_ARQUIVO))

			BEGIN TRAN @OcorrenciaTransaction
			BEGIN TRY
				-- insere ocorrencia para pf
				IF @ID_TIPO_CLIENTE = 1
				BEGIN
					EXEC sp_InsertOcorrenciaPf @ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO,
												@ANO = @ANO,
												@ID_OPERACAO = @ID_OPERACAO,
												@ID_CLIENTE = @ID_CLIENTE,
												@REGISTRO = @REGISTRO
				END -- END IF
				-- insere ocorrencia para pj
				ELSE
				BEGIN
					EXEC sp_InsertOcorrenciaPj @ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO,
												@ANO = @ANO,
												@TRIMESTRE = @TRIMESTRE,
												@ID_OPERACAO = @ID_OPERACAO,
												@ID_CLIENTE = @ID_CLIENTE,
												@REGISTRO = @REGISTRO
				
				END -- END ELSE
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
						'ID_SPEC_ARQUIVO: ' + CONVERT(VARCHAR, @ID_SPEC_ARQUIVO) + '; ANO: ' + CONVERT(VARCHAR, @ANO) + ';TRIMESTRE: ' + @TRIMESTRE + '; ID_CLIENTE: ' + CONVERT(VARCHAR, @ID_CLIENTE),
						@REGISTRO
					 )
				COMMIT
			END CATCH
		END -- ELSE -> IF CONVERT(BIGINT, @CD_CLIENTE) = 0
		
		-- BUSCA AS INFORMAÇÕES DO PRÓXIMO REGISTRO
		FETCH NEXT FROM rCURSOR INTO @REGISTRO
	END -- (FIM DO LOOP NOS REGISTROS SELECIONADOS) ///

	-- note the user of a GOTO/LABEL ... ewwww 
	/*ERROR_HANDLER: 
	 -- Rollback if the transaction is still around
	 IF @@TRANCOUNT>0 ROLLBACK*/

	-- FECHA E DESTROI O CURSOR
	CLOSE rCURSOR
	DEALLOCATE rCURSOR
	--///
END
