CREATE PROCEDURE [dbo].[spOdobriZahtev]
	@korime varchar(100)
AS
BEGIN
	declare @regbr varchar(100);
	select @regbr=RegBr from ZahtevZaKurira where Korime=@korime;

	delete from ZahtevZaKurira where Korime=@korime;

	insert into Kurir(Korime,RegBr,BrIsporucenihPaketa,Status) values(@korime,@regbr,0,0);
END
