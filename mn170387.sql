

CREATE TABLE [Admin]
( 
	[KorIme]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdGrad]             integer  IDENTITY  NOT NULL ,
	[PostanskiBr]        varchar(100)  NULL ,
	[Naziv]              varchar(100)  NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[KorIme]             varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NULL ,
	[Ime]                varchar(100)  NULL ,
	[Prezime]            varchar(100)  NULL ,
	[BrPoslatihPaketa]   integer  NULL 
	CONSTRAINT [PocetnaVrednostNula_194663088]
		 DEFAULT  0
	CONSTRAINT [VeceOdNula_72768437]
		CHECK  ( BrPoslatihPaketa >= 0 )
)
go

CREATE TABLE [Kurir]
( 
	[KorIme]             varchar(100)  NOT NULL ,
	[RegBr]              varchar(100)  NULL ,
	[BrIsporucenihPaketa] integer  NULL 
	CONSTRAINT [PocetnaVrednostNula_812155780]
		 DEFAULT  0,
	[Profit]             decimal(10,3)  NULL 
	CONSTRAINT [PocetnaVrednostNula_772454611]
		 DEFAULT  0,
	[Status]             integer  NULL 
	CONSTRAINT [StatusKurir_1141277533]
		CHECK  ( Status BETWEEN 0 AND 1 )
)
go

CREATE TABLE [Opstina]
( 
	[IdOpstina]          integer  IDENTITY  NOT NULL ,
	[Naziv]              varchar(100)  NULL ,
	[Xkoor]              integer  NULL ,
	[Ykoor]              integer  NULL ,
	[IdGrad]             integer  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdPaket]            integer  IDENTITY  NOT NULL ,
	[TipPak]             integer  NULL 
	CONSTRAINT [TipPaketa_370342676]
		CHECK  ( TipPak BETWEEN 0 AND 2 ),
	[Tezina]             decimal(10,3)  NULL ,
	[OpstinaOd]          integer  NOT NULL ,
	[OpstinaDo]          integer  NOT NULL ,
	[Korisnik]           varchar(100)  NOT NULL ,
	[Kurir]              varchar(100)  NULL ,
	[StatusIsporuke]     integer  NULL 
	CONSTRAINT [PocetnaVrednostNula_1600174844]
		 DEFAULT  0
	CONSTRAINT [StatusIsporuke_595929054]
		CHECK  ( StatusIsporuke BETWEEN 0 AND 3 ),
	[Cena]               decimal(10,3)  NULL ,
	[VremePrihv]         datetime  NULL 
)
go

CREATE TABLE [PonudaZaVoznju]
( 
	[IdPonuda]           integer  IDENTITY  NOT NULL ,
	[Procenat]           decimal(10,3)  NULL ,
	[KurirKorime]        varchar(100)  NOT NULL ,
	[IdPaket]            integer  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[RegBr]              varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NULL 
	CONSTRAINT [TipGoriva_2021023082]
		CHECK  ( TipGoriva BETWEEN 0 AND 2 ),
	[Potrosnja]          decimal(10,3)  NULL 
)
go

CREATE TABLE [ZahtevZaKurira]
( 
	[KorIme]             varchar(100)  NOT NULL ,
	[RegBr]              varchar(100)  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdGrad] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK1Grad] UNIQUE ([PostanskiBr]  ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK2Grad] UNIQUE ([Naziv]  ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdOpstina] ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdPaket] ASC)
go

ALTER TABLE [PonudaZaVoznju]
	ADD CONSTRAINT [XPKPonudaZaVoznju] PRIMARY KEY  CLUSTERED ([IdPonuda] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([RegBr] ASC)
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [XPKZahtevZaKurira] PRIMARY KEY  CLUSTERED ([KorIme] ASC,[RegBr] ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([RegBr]) REFERENCES [Vozilo]([RegBr])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdGrad]) REFERENCES [Grad]([IdGrad])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_7] FOREIGN KEY ([OpstinaOd]) REFERENCES [Opstina]([IdOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_8] FOREIGN KEY ([OpstinaDo]) REFERENCES [Opstina]([IdOpstina])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_12] FOREIGN KEY ([Korisnik]) REFERENCES [Korisnik]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [PonudaZaVoznju]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([KurirKorime]) REFERENCES [Kurir]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [PonudaZaVoznju]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IdPaket]) REFERENCES [Paket]([IdPaket])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([RegBr]) REFERENCES [Vozilo]([RegBr])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


CREATE PROCEDURE [dbo].[spOdobriZahtev]
	@korime varchar(100)
AS
BEGIN
	declare @regbr varchar(100);
	select @regbr=RegBr from ZahtevZaKurira where Korime=@korime;

	delete from ZahtevZaKurira where Korime=@korime;

	insert into Kurir(Korime,RegBr,BrIsporucenihPaketa,Status) values(@korime,@regbr,0,0);
END

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

