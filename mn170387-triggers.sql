CREATE TRIGGER TR_TransportOffer_ObrisiPonude
   ON  dbo.Paket
   AFTER UPDATE
AS 
BEGIN
	
	declare @idpaket int, @status int;
	declare @kursor cursor;
	set @kursor = cursor for select IdPaket, StatusIsporuke from inserted;

	open @kursor;

	fetch from @kursor into @idpaket, @status

	while @@FETCH_STATUS=0 
	begin
		if(@status=1) 
			begin 
				delete from PonudaZaVoznju where IdPaket=@idpaket
			end
		fetch from @kursor into @idpaket, @status
	end

	close @kursor;
	deallocate @kursor;

END
GO
