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
ALTER PROCEDURE usp_GetPropertyValue
	@NO_PROPRIEDADE VARCHAR(255),
	@REGISTRO VARCHAR(8000),
	@ID_SPEC_ARQUIVO BIGINT,
	@VALUE NVARCHAR(4000) OUT
AS
BEGIN
	DECLARE @INIT_POS INT,
			@TAMANHO INT
	
	IF OBJECT_ID('tempdb..##REG_DELIMITER') IS NULL
		CREATE TABLE ##REG_DELIMITER(
			ID_SPEC_ARQUIVO BIGINT,
			NO_PROPRIEDADE VARCHAR(255),
			INIT_POS INT,
			TAMANHO INT
		)
	
	SELECT @INIT_POS = INIT_POS, @TAMANHO = TAMANHO 
		FROM ##REG_DELIMITER 
	WHERE ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO AND NO_PROPRIEDADE = @NO_PROPRIEDADE

	IF @INIT_POS IS NULL
	BEGIN
		SET @INIT_POS = (SELECT SUM(P.VL_TAMANHO) 
        FROM PROPRIEDADE P
            INNER JOIN SPEC_ARQUIVO_PROPRIEDADE SAP
            ON (SAP.ID_PROPRIEDADE = P.ID_PROPRIEDADE AND SAP.ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO)
        WHERE P.ID_LAYOUT_ARQUIVO = 3 AND P.NU_ORDEM < (SELECT P.NU_ORDEM FROM PROPRIEDADE P
                            INNER JOIN SPEC_ARQUIVO_PROPRIEDADE SAP
                            ON (SAP.ID_PROPRIEDADE = P.ID_PROPRIEDADE AND SAP.ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO) 
                            WHERE P.NO_PROPRIEDADE LIKE @NO_PROPRIEDADE))
                         
		IF(@INIT_POS IS NULL)   
    		SET @INIT_POS =  1
		ELSE SET @INIT_POS = @INIT_POS+1
		
		SET @TAMANHO = (SELECT P.VL_TAMANHO FROM PROPRIEDADE P
								INNER JOIN SPEC_ARQUIVO_PROPRIEDADE SAP
								ON (SAP.ID_PROPRIEDADE = P.ID_PROPRIEDADE AND SAP.ID_SPEC_ARQUIVO = @ID_SPEC_ARQUIVO) 
							WHERE P.ID_LAYOUT_ARQUIVO = 3 AND P.NO_PROPRIEDADE LIKE @NO_PROPRIEDADE)
		
		INSERT INTO ##REG_DELIMITER VALUES (@ID_SPEC_ARQUIVO, @NO_PROPRIEDADE, @INIT_POS, @TAMANHO)
	END
	
	SET @VALUE = SUBSTRING(@REGISTRO, @INIT_POS, @TAMANHO)	

END
GO
