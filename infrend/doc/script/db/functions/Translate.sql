USE [infrend]
GO
/****** Object:  UserDefinedFunction [dbo].[Translate]    Script Date: 03/08/2012 08:52:28 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE FUNCTION [dbo].[Translate] (
	@String NVARCHAR(500),		-- The string from which you want certain characters to be replaced or removed
	@SearchFor NVARCHAR(100),	-- A string of characters you want to be replaced or removed, like 'ç'
	@ReplaceBy NVARCHAR(100)	-- A string of character you want the @SearchFor characters be replaced with.
								-- A character with index i in @SearchFor is replaced with the character with 
								-- index i in @ReplaceBy.
								-- When there is no corresponding @ReplaceBy character at index i (there are more 
								-- characters in @SearchFor than in @ReplaceBy), the @SearchFor character is removed
)
RETURNS NVARCHAR(500)
AS
BEGIN
	if @String IS NULL
		RETURN(NULL)

	if @SearchFor IS NULL AND @ReplaceBy IS NULL
	BEGIN	-- provide some standard 
		SET @SearchFor = 'áâãäæèïéìëíîçåñòóôöõàøúüûùýÁÂÃÄÆÈÏÉÌËÍÎÅÑÒÓÔÖÕÀØÚÜÛÙÝ'
		SET @ReplaceBy = 'aaaaaeieieiicanoooooaouuuuyAAAAAEIEIEIIANOOOOOAOUUUUY'
	END
	ELSE 
	BEGIN 
		if @ReplaceBy IS NULL
			RETURN(NULL)

		if @SearchFor IS NULL
			RETURN(NULL)
	END

-- Suppose a character 'X' is present (one time or multiple times) in @String AND in @SearchFor. 
-- And the index of the character 'X' in @SearchFor is I. 
-- The function will replace all occurrences of the character 'X' in @String with the corresponding 
-- character Y at the index I in @ReplaceBy.
-- When no characters of @SearchFor are present in @String, the function returns @String unaltered.
-- When there are more characters in @SearchFor than in @ReplaceBy (I > len(@ReplaceBy)), those extra 
-- characters in @SearchFor are removed from @String (replaced by an empty string). 
-- There may not be more characters in @ReplaceBy than in @SearchFor. 
	if len(@ReplaceBy) > len(@SearchFor)
		RETURN(NULL)

	DECLARE @SearchForIndex INT
	DECLARE @SearchChar NVARCHAR(1)
	DECLARE @ReplaceChar NVARCHAR(1)

	SET @SearchForIndex = 1

	-- for all characters in @SearchFor
	WHILE @SearchForIndex <= len(@SearchFor)
	BEGIN
		-- get the current character from @SearchFor
		SET @SearchChar = Substring(@SearchFor, @SearchForIndex, 1)
		-- If the current @SearchFor character is also present in @String
		-- Use a case sensitive collation, otherwise 'François' gets translated to 'FRANCOIS'
		if @String COLLATE Latin1_General_BIN LIKE '%'+@SearchChar+'%'
		begin
			-- Replace all occurrences of the current @SearchFor character in @String
			-- by the corresponding character (same index) in @ReplaceBy.
			-- BUT when there is no corresponding character in @ReplaceBy 
			-- (@SearchForIndex > len(@ReplaceBy)), replace the @SearchFor character in @String 
			-- in @String by the empty string '' (remove them)
			if @SearchForIndex <= len(@ReplaceBy) 
				SET @ReplaceChar = Substring(@ReplaceBy, @SearchForIndex, 1)
			else
				SET @ReplaceChar = ''

			SET @String = REPLACE(@String COLLATE Latin1_General_BIN, @SearchChar, @ReplaceChar)
		end
		SET @SearchForIndex = @SearchForIndex + 1
	END
	RETURN(@String)
END
