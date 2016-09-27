-- -------------------------------------------------------------------------
-- INDISTAC-863 - Evitar que el unitMultiplier pueda ser nulo en BBDD
-- -------------------------------------------------------------------------

ALTER TABLE TB_QUANTITIES add UNIT_MULTIPLIER_FK NUMBER(19);

update TB_QUANTITIES quantity
set unit_multiplier_fk = (select mult.id
                        from tb_lis_units_multipliers mult
                         where mult.unit_multiplier = quantity.unit_multiplier)
where unit_multiplier is not null;


update TB_QUANTITIES quantity
set unit_multiplier_fk = (select mult.id
                        from tb_lis_units_multipliers mult
                         where mult.unit_multiplier = 1)
where unit_multiplier is null;


ALTER TABLE TB_QUANTITIES modify UNIT_MULTIPLIER_FK NUMBER(19) NOT NULL;

alter table tb_quantities drop column unit_multiplier;


commit;
