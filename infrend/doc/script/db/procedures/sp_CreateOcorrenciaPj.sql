USE [infrend]
GO
/****** Object:  StoredProcedure [dbo].[sp_CreateOcorrenciaPj]    Script Date: 03/08/2012 08:51:12 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [dbo].[sp_CreateOcorrenciaPj] (
	@ID_SPEC_ARQUIVO BIGINT,
    @ANO INT
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
  
  DECLARE 	@ID BIGINT,
			@REGISTRO VARCHAR(8000),
			@CD_CLIENTE VARCHAR(15),
            @CD_AGENCIA VARCHAR(3),
            @CD_CONTA VARCHAR(6),
            @MES INT,
            @OPERACAO BIGINT,
            @RENDIMENTOS VARCHAR(288),
            @INC INT,
            @POSICAO INT,
            @RENDIMENTO VARCHAR(12),
            @IMPOSTO VARCHAR(12)

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
		IF NOT EXISTS (SELECT C.CD_CLIENTE 
							FROM CLIENTE C 
						WHERE C.CD_CLIENTE 
                        LIKE @CD_CLIENTE)
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
				2,
				dbo.fc_SubStringIn(@REGISTRO, 'nome', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'logradouro', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'bairro', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'cidade', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'uf', @ID_SPEC_ARQUIVO),
				dbo.fc_SubStringIn(@REGISTRO, 'cep', @ID_SPEC_ARQUIVO)
            )
		END
        
        SET @CD_AGENCIA = dbo.fc_SubStringIn(@REGISTRO, 'agencia', @ID_SPEC_ARQUIVO)
        SET @CD_CONTA = dbo.fc_SubStringIn(@REGISTRO, 'conta', @ID_SPEC_ARQUIVO)
        
        IF NOT EXISTS (SELECT *	FROM DADO_BANCARIO D
                            WHERE 
                            D.CD_AGENCIA LIKE @CD_AGENCIA 
                            AND 
                            D.CD_CONTA LIKE @CD_CONTA)
        BEGIN
        	INSERT INTO 
            DADO_BANCARIO(
            	CD_AGENCIA,
                CD_CONTA,
                CD_DIGITO,
                CD_CLIENTE
            ) 
            VALUES(
            	@CD_AGENCIA,
                @CD_CONTA,
                dbo.fc_SubStringIn(@REGISTRO, 'digito', @ID_SPEC_ARQUIVO),
                @CD_CLIENTE
            )
        END

		SET @MES = 1
        SET @OPERACAO = CONVERT(BIGINT, dbo.fc_SubStringIn(@REGISTRO, 'operacao', @ID_SPEC_ARQUIVO))
        SET @RENDIMENTOS = dbo.fc_SubStringIn(@REGISTRO, 'rendimento', @ID_SPEC_ARQUIVO)
        SET @INC = 12
        SET @POSICAO = 1

		WHILE(@MES <=12)
        BEGIN
            SET @RENDIMENTO = SUBSTRING(@RENDIMENTOS, @POSICAO, @POSICAO+@INC)
            
            SET @RENDIMENTOS  = SUBSTRING(@RENDIMENTOS, @POSICAO+@INC+1, LEN(@RENDIMENTOS))
            
            SET @IMPOSTO = SUBSTRING(@RENDIMENTOS, @POSICAO, @POSICAO+@INC)
            
            SET @RENDIMENTOS  = SUBSTRING(@RENDIMENTOS, @POSICAO+@INC+1, LEN(@RENDIMENTOS))
        
        	INSERT INTO 
            OCORRENCIA_PJ(
                CD_AGENCIA,
                CD_CONTA,
                AN_OCORRENCIA,
                ME_OCORRENCIA,
                ID_OPERACAO,
                VL_RENDIMENTO,
                VL_IMPOSTO_RENDA
            ) 
            VALUES(
                @CD_AGENCIA,
                @CD_CONTA,
                @ANO,
                @MES,
                @OPERACAO,
                CONVERT(NUMERIC(10,2), SUBSTRING(@RENDIMENTO, 1, LEN(@RENDIMENTO)-1)+'.'+SUBSTRING(@RENDIMENTO, LEN(@RENDIMENTO)-1, LEN(@RENDIMENTO))),
            	CONVERT(NUMERIC(10,2), SUBSTRING(@RENDIMENTO, 1, LEN(@RENDIMENTO)-1)+'.'+SUBSTRING(@RENDIMENTO, LEN(@RENDIMENTO)-1, LEN(@RENDIMENTO)))
            )	
        	
        	SET @MES = @MES + 1
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
