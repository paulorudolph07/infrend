USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[usp_InsertOcorrenciaPf]    Script Date: 02/08/2013 09:55:32 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
 /**************************************************************************   
  * FUNÇÃO: LOOP NOS REGISTROS DA TABELA REGISTRO_ARQUIVO PARA CRIACAO DE  *
  * OCORRENCIAS                                                            *
  *------------------------------------------------------------------------*
  * CRIADA EM    : 17/02/2012 - PAULO RUDOLPH                              *
  *------------------------------------------------------------------------*
  * ÚLTIMAS ATUALIZAÇÕES:                                                  *
  * - 31/03/2011 - PAULO RUDOLPH                                           *
  *              - REVISÃO DE LIMPEZA DE VARIÁVEIS.                        *
  * - 17/12/2012 - PAULO RUDOLPH					                       *
  *              - ADEQUAÇÃO AS NOVAS TABELAS                              *  
  **************************************************************************/
ALTER PROCEDURE [dbo].[usp_InsertOcorrenciaPf] 
	-- Add the parameters for the stored procedure here
	@ID_SPEC_ARQUIVO BIGINT,
	@ANO INT,
	@ID_OPERACAO BIGINT,
	@ID_DADO_BANCARIO BIGINT,
	@REGISTRO VARCHAR(8000)
AS
BEGIN

	DECLARE @SALDO_ANTERIOR VARCHAR(10),
			@SALDO_ATUAL VARCHAR(10),
			@RENDIMENTO VARCHAR(10),
			@ID_OCORRENCIA BIGINT,
			
			@ErrorMessage NVARCHAR(4000),
			@ErrorSeverity INT,
			@ErrorState INT

	BEGIN TRY
		/*SET @SALDO_ANTERIOR = dbo.fc_SubStringIn(@REGISTRO, 'saldoAnterior', @ID_SPEC_ARQUIVO)
		SET @SALDO_ATUAL = dbo.fc_SubStringIn(@REGISTRO, 'saldoAtual', @ID_SPEC_ARQUIVO)
		SET @RENDIMENTO = dbo.fc_SubStringIn(@REGISTRO, 'rendimento', @ID_SPEC_ARQUIVO)*/
		exec usp_GetPropertyValue 'saldoAnterior', @REGISTRO, @ID_SPEC_ARQUIVO, @SALDO_ANTERIOR OUT
		exec usp_GetPropertyValue 'saldoAtual', @REGISTRO, @ID_SPEC_ARQUIVO, @SALDO_ATUAL OUT
		exec usp_GetPropertyValue 'rendimento', @REGISTRO, @ID_SPEC_ARQUIVO, @RENDIMENTO OUT
	
		INSERT INTO OCORRENCIA_PF(
			ID_DADO_BANCARIO,
			AN_OCORRENCIA,
			ID_OPERACAO,
			VL_SALDO_ANTERIOR,
			VL_SALDO_ATUAL,
			VL_RENDIMENTO
		)
		VALUES(
			@ID_DADO_BANCARIO,
			@ANO,
			@ID_OPERACAO,
			dbo.fc_SetSignal(@SALDO_ANTERIOR, 2),
			dbo.fc_SetSignal(@SALDO_ATUAL, 2),
			dbo.fc_SetSignal(@RENDIMENTO, 2)
		)
	END TRY
	BEGIN CATCH
		IF ERROR_NUMBER() = 2627 -- Violation of UNIQUE KEY constr
		BEGIN -- IF					
			-- CASO A OCORRENCIA JA EXISTA PARA OUTRO TIPO DE OPERAÇÃO COM MESMO ID (EX.: FUNDOS E FUNDOS_NOR)
			SET @ID_OCORRENCIA = (SELECT O.ID_OCORRENCIA_PF FROM OCORRENCIA_PF O
								WHERE O.ID_DADO_BANCARIO = @ID_DADO_BANCARIO
								AND O.ID_OPERACAO = @ID_OPERACAO
								AND O.AN_OCORRENCIA = @ANO)
			UPDATE OCORRENCIA_PF 
				SET 
					VL_SALDO_ANTERIOR = (SELECT VL_SALDO_ANTERIOR FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@SALDO_ANTERIOR, 2),
					VL_SALDO_ATUAL = (SELECT VL_SALDO_ATUAL FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@SALDO_ATUAL, 2),
					VL_RENDIMENTO = (SELECT VL_RENDIMENTO FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@RENDIMENTO, 2)
				WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA 
		END -- IF
		ELSE
		BEGIN -- ELSE
			SELECT 
					@ErrorMessage = ERROR_MESSAGE(),
					@ErrorSeverity = ERROR_SEVERITY(),
					@ErrorState = ERROR_STATE()
			
			RAISERROR(@ErrorMessage, @ErrorSeverity, @ErrorState)
		END -- ELSE
		
	END CATCH

END
