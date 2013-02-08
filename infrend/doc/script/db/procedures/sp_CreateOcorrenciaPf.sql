USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[sp_CreateOcorrenciaPf]    Script Date: 03/08/2012 08:24:07 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
ALTER PROCEDURE [dbo].[sp_CreateOcorrenciaPf] 
	-- Add the parameters for the stored procedure here
	@ID_SPEC_ARQUIVO BIGINT,
	@ANO INT
AS
BEGIN
	
	DECLARE @ID BIGINT,
			@REGISTRO VARCHAR(8000),
			@CD_CLIENTE VARCHAR(15),
			@ID_AGENCIA BIGINT,
            @NU_AGENCIA VARCHAR(3),
			@ID_CONTA BIGINT,
            @NU_CONTA VARCHAR(6),
			@NU_DIGITO VARCHAR(1),
            @ID_OPERACAO BIGINT,
			@SALDO_ANTERIOR VARCHAR(10),
			@SALDO_ATUAL VARCHAR(10),
            @RENDIMENTO VARCHAR(10),
			@ID_OCORRENCIA BIGINT

	-- INICIALIZA AS VARIÁVEIS
	SET @ID = NULL
    SET @REGISTRO = NULL
	-- ///

	-- MONTA O CURSOR DE REGISTROS DA TABELA REGISTRO_ARQUIVO
	DECLARE rCURSOR CURSOR LOCAL FOR
		SELECT R.ID_REGISTRO_ARQUIVO, R.TX_REGISTRO
			FROM REGISTRO_ARQUIVO R
	-- ///

	-- ABRE O CURSOR
	OPEN rCURSOR

	-- BUSCA AS INFORMAÇÕES DO PRIMEIRO REGISTRO
	FETCH NEXT FROM rCURSOR INTO @ID, @REGISTRO
    
	-- LOOP NOS REGISTROS SELECIONADOS
	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		SET @CD_CLIENTE = dbo.fc_SubStringIn(@REGISTRO, 'codigo', @ID_SPEC_ARQUIVO)
		IF NOT EXISTS (SELECT C.CD_CLIENTE FROM CLIENTE C WHERE C.CD_CLIENTE = @CD_CLIENTE)
		BEGIN
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
				1,
				dbo.fc_SubStringIn(@REGISTRO, 'nome', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'logradouro', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'bairro', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'cidade', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'uf', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'cep', @ID_SPEC_ARQUIVO)
            )
		END
        
        SET @NU_AGENCIA = dbo.fc_SubStringIn(@REGISTRO, 'agencia', @ID_SPEC_ARQUIVO)
		SET @ID_AGENCIA = (SELECT A.ID_AGENCIA FROM AGENCIA A WHERE A.NU_AGENCIA = @NU_AGENCIA)

        IF @ID_AGENCIA IS NULL
        BEGIN
        	INSERT INTO AGENCIA (NU_AGENCIA) VALUES(@NU_AGENCIA)
			SET @ID_AGENCIA = @@IDENTITY
        END

		SET @NU_CONTA = dbo.fc_SubStringIn(@REGISTRO, 'conta', @ID_SPEC_ARQUIVO)
		SET @NU_DIGITO = dbo.fc_SubStringIn(@REGISTRO, 'digito', @ID_SPEC_ARQUIVO)
		SET @ID_CONTA = (SELECT C.ID_CONTA FROM CONTA C 
							WHERE C.NU_CONTA = @NU_CONTA 
							AND C.ID_AGENCIA = @ID_AGENCIA 
							AND C.NU_DIGITO = @NU_DIGITO)

		IF @ID_CONTA IS NULL
        BEGIN
        	INSERT INTO CONTA(
				ID_AGENCIA, 
				NU_CONTA, 
				NU_DIGITO,
				CD_CLIENTE) 
			VALUES(
				@ID_AGENCIA, 
				@NU_CONTA, 
				@NU_DIGITO,
				@CD_CLIENTE
			)
			SET @ID_CONTA = @@IDENTITY
        END
		
        SET @ID_OPERACAO = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'operacao', @ID_SPEC_ARQUIVO))
		SET @SALDO_ANTERIOR = dbo.fc_SubStringIn(@REGISTRO, 'saldoAnterior', @ID_SPEC_ARQUIVO)
		SET @SALDO_ATUAL = dbo.fc_SubStringIn(@REGISTRO, 'saldoAtual', @ID_SPEC_ARQUIVO)
        SET @RENDIMENTO = dbo.fc_SubStringIn(@REGISTRO, 'rendimento', @ID_SPEC_ARQUIVO)

		SET @ID_OCORRENCIA = (SELECT O.ID_OCORRENCIA_PF FROM OCORRENCIA_PF O
								WHERE O.ID_CONTA = @ID_CONTA
								AND O.ID_OPERACAO = @ID_OPERACAO
								AND O.AN_OCORRENCIA = @ANO)

		IF @ID_OCORRENCIA IS NULL
			BEGIN
        		INSERT INTO 
				OCORRENCIA_PF(
					ID_CONTA,
					AN_OCORRENCIA,
					ID_OPERACAO,
					VL_SALDO_ANTERIOR,
					VL_SALDO_ATUAL,
					VL_RENDIMENTO
				)
				VALUES(
					@ID_CONTA,
					@ANO,
					@ID_OPERACAO,
					dbo.fc_SetSignal(@SALDO_ANTERIOR, 2),
					dbo.fc_SetSignal(@SALDO_ATUAL, 2),
					dbo.fc_SetSignal(@RENDIMENTO, 2)
				)
			END
		ELSE
		BEGIN
			UPDATE OCORRENCIA_PF 
				SET 
					VL_SALDO_ANTERIOR = (SELECT VL_SALDO_ANTERIOR FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@SALDO_ANTERIOR, 2),
					VL_SALDO_ATUAL = (SELECT VL_SALDO_ATUAL FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@SALDO_ATUAL, 2),
					VL_RENDIMENTO = (SELECT VL_RENDIMENTO FROM OCORRENCIA_PF WHERE ID_OCORRENCIA_PF = @ID_OCORRENCIA) +
						dbo.fc_SetSignal(@RENDIMENTO, 2)
		END
		
		-- BUSCA AS INFORMAÇÕES DO PRÓXIMO REGISTRO
		FETCH NEXT FROM rCURSOR INTO @ID, @REGISTRO
	END
	-- (FIM DO LOOP NOS REGISTROS SELECIONADOS) ///

	-- FECHA E DESTROI O CURSOR
	CLOSE rCURSOR
	DEALLOCATE rCURSOR
	--///

	--COMMIT TRAN

	/*END TRY

	BEGIN CATCH
		ROLLBACK TRAN
			SELECT -ERROR_NUMBER()
	END CATCH*/

END
