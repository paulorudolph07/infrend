delete from registro_arquivo
delete from ocorrencia_pf
delete from ocorrencia_pj
delete from lg_processamento
delete from arquivo_processado
delete from cliente

DBCC CHECKIDENT ('cliente', RESEED, 0)
DBCC CHECKIDENT ('arquivo_processado', RESEED, 0)
DBCC CHECKIDENT ('ocorrencia_pf', RESEED, 0)
DBCC CHECKIDENT ('ocorrencia_pj', RESEED, 0)
DBCC CHECKIDENT ('lg_processamento', RESEED, 0)
