
/****** Object:  StoredProcedure [dbo].[SEL_user]    Script Date: 18/12/2019 12:30:43 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[SEL_user]
	  @tx_action_name		varchar(32)		= NULL

	, @is_active			int						= 1
	, @id_env_key			int						= NULL
	, @id_user_mod_key		int						= NULL
	, @dtt_mod				datetime				= NULL

	, @id_state_key			int						= NULL
	, @id_action_key		int						= NULL
	, @dtt_valid_from		datetime				= NULL
	, @dtt_valid_to			datetime				= NULL

	, @dtt_as_at			datetime				= NULL
	, @dtt_last_refresh		datetime				= NULL
	, @id_user_key				int								= NULL	
	, @id_user_ver					int								= NULL
	, @id_group_key				int								= NULL
	, @tx_first_name			varchar(64)			= NULL
	, @tx_last_name				varchar(64)			= NULL
	, @tx_login_name			varchar(32)			= NULL
	, @tx_password				varchar(8000)			= NULL
	, @tx_user_alias			varchar(32)			= NULL

	, @tx_group_name			varchar(64)			= NULL

	, @is_full_details			bit								= 0

	 , @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT

AS
	-- GLOBAL VARS --
	DECLARE	  @g_tx_action_name_orig	varchar(32)
			, @g_tx_state_name_orig		varchar(64)

			, @g_id_env_key				int
			, @g_tx_env_name			varchar(256)

			, @g_ct_row					int
			, @g_id_return_status		int

			, @g_id_error_key			int
			, @g_tx_err_msg				varchar(1024)
			, @g_tx_err_msg_tmp			varchar(1024)
			, @g_is_outer_sp			bit

	DECLARE   @g_dt_sys_today			date
			, @g_dt_sys_prev_day		date
			, @g_dt_sys_next_day		date
			, @g_dt_sys_prev_biz_day	date
			, @g_dt_sys_next_biz_day	date

	

	--	DECLARE @g_in_tran		int
	DECLARE	@g_is_ext_txn	bit
	DECLARE @g_is_sp_txn	bit

	SELECT @g_is_ext_txn = 0, @g_is_sp_txn = 0

	DECLARE @g_tmp_int		int

	DECLARE   @g_dtt_log			datetime
	 		, @g_tx_tmp_log_msg		varchar(MAX)
	 		, @g_tx_log_msg			varchar(MAX)

		  	, @g_id_err_num			int
			, @g_id_err_sev			int
			, @g_id_err_state		int
			, @g_tx_sp_name			varchar(128)
			, @g_id_line_num		int
			, @g_id_log_level		int

			, @g_is_record_time		bit
			, @g_is_print_msg		bit
			, @g_is_persist_msg  	bit

			, @g_tx_json_log_msg	varchar(MAX)

	SELECT	@g_tx_json_log_msg = ''

	DECLARE @g_dtt_proc_start		datetime
	DECLARE @g_dtt_proc_end			datetime
	DECLARE @g_dtt_tot_elapsed		int

	DECLARE	@g_dtt_query_start		datetime
	DECLARE	@g_dtt_query_end		datetime
	DECLARE @g_dtt_query_elapsed	int

	DECLARE @g_tmp_tx_tot_time		varchar(255)
	DECLARE @g_tmp_tx_query_time	varchar(255)

	IF ( (@@NESTLEVEL = 1) )
	BEGIN
		IF ( ((@@OPTIONS & 2) != 2) AND (@@TRANCOUNT = 0) )
		BEGIN
			SELECT @g_is_ext_txn = 0
		END
		ELSE
		BEGIN
			SELECT @g_is_ext_txn = 1
		END
	END

	IF (((@tx_log_level IS NULL) OR (@tx_log_level = '?')) OR ((@is_persist_msg IS NULL) OR (@is_persist_msg = -2147483648)) OR ((@is_record_time IS NULL) OR (@is_record_time = -2147483648)))
	BEGIN
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='SEL_user', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[SEL_user] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'SEL_user.sp', @g_id_line_num = 133

	-- TODO_H : [Naz] Move to Function

	IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
	BEGIN
		IF (2 >= @g_id_log_level)
		BEGIN
			IF (@g_is_print_msg = 1)
			BEGIN
				SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_err_msg, '?')
				PRINT  @g_tx_tmp_log_msg
			END

			IF ( (@g_is_persist_msg = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
													   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
													   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_err_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
			
				IF (@g_id_return_status != 0)
				BEGIN
					RAISERROR ('Error calling ACT_msg_log', 16, 1)
					IF (@g_is_sp_txn = 1)
					BEGIN
						SELECT @g_is_sp_txn = 0
						ROLLBACK TRANSACTION
				
						IF ( (@@NESTLEVEL = 1) )
						BEGIN
							EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
					
							IF (@g_id_return_status != 0)
							BEGIN
								PRINT 'ERROR SAVING JSON LOG MSG!'
							END
						END
					END
					IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
					BEGIN
						-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
				
						IF (30008 = 0)
						BEGIN
							--RAISERROR (@tx_json_log_msg, 10, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
				
						END
						ELSE
						BEGIN
							--RAISERROR (@tx_json_log_msg, 16, 1)
							EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
						END
					END
				
					RETURN 30008
				END
			END
		END
	END

	EXEC @g_id_return_status = GET_environment @id_env_key = @g_id_env_key OUTPUT, @tx_env_name	= @g_tx_env_name OUTPUT, @id_user_key = @id_user_mod_key

	SELECT @g_tx_err_msg = 'Error calling SP -> [GET_ENVIRONMENT] '

	IF (@g_id_return_status != 0)
	BEGIN
		RAISERROR (@g_tx_err_msg, 16, 1)
		IF (@g_is_sp_txn = 1)
		BEGIN
			SELECT @g_is_sp_txn = 0
			ROLLBACK TRANSACTION
	
			IF ( (@@NESTLEVEL = 1) )
			BEGIN
				EXEC @g_id_return_status = ACT_msg_log @tx_action_name = 'LOG_JSON', @tx_json_log_msg = @tx_json_log_msg, @ct_nestlevel = @@NESTLEVEL
		
				IF (@g_id_return_status != 0)
				BEGIN
					PRINT 'ERROR SAVING JSON LOG MSG!'
				END
			END
		END
		IF ( (@@NESTLEVEL = 1) AND (@g_is_ext_txn = 1) )
		BEGIN
			-- SELECT @tx_json_log_msg = 'JSON_LOG:[' + @tx_json_log_msg + ']'
	
			IF (30001 = 0)
			BEGIN
				--RAISERROR (@tx_json_log_msg, 10, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 10
	
			END
			ELSE
			BEGIN
				--RAISERROR (@tx_json_log_msg, 16, 1)
				EXEC SYS_split_raise_error @tx_data_orig = @tx_json_log_msg, @tx_separator = 'DB_NEW_LINE', @id_severity_key = 16
			END
		END
	
		RETURN 30001
	END

	IF ( ((@dtt_last_refresh IS NULL) OR (@dtt_last_refresh = '01/01/1970')) )
	BEGIN
		SELECT @dtt_last_refresh = '01/01/1970'
	END

	IF (  (NOT  ((@id_user_key IS NULL) OR (@id_user_key = -2147483648)))  AND ( NOT ((@id_user_ver IS NULL) OR (@id_user_ver = -2147483648))) )
	BEGIN
		-- Assumption is that it is a version history request, ensure its not the latest version that is being requesting
		IF ( NOT EXISTS (SELECT 1 FROM T_USER WHERE id_user_key = @id_user_key AND id_user_ver = @id_user_ver) )
		BEGIN
			-- A historic request......
			SELECT	@dtt_as_at = dtt_mod
			FROM	T_USER_AUDIT	U
			WHERE	id_user_key = @id_user_key
			AND		is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)
		END
	END


	IF ( ((@dtt_as_at IS NULL) OR (@dtt_as_at = '01/01/1970')) )
	BEGIN
		-- User Details
		SELECT	  tx_rs_type = 'RS_TYPE_USER'
				, USR.*		--Removed two fields [id_state_key and id_action_key, to resolve issue in middle tier]
				, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) AS tx_password_ref
--				, ENV.id_env_key
	--			, UMOD.tx_login_name	as tx_user_mod_name
				, LE.tx_legal_entity_id
				, LI.is_logged_in
				, LI.tx_authentication_code
		FROM		T_USER			USR
		JOIN		T_GROUP			GRP		ON	GRP.id_group_key		= USR.id_group_key
		LEFT JOIN 	T_LEGAL_ENTITY 	LE 		ON 	LE.id_legal_entity_key 	= USR.id_legal_entity_key
		LEFT JOIN 	T_LOGIN 		LI 		ON 	LI.id_user_key 			= USR.id_user_key
--		JOIN	T_ENVIRONMENT	ENV		ON	USR.id_env_key		= ENV.id_env_key
--		JOIN	T_USER			UMOD	ON	UMOD.id_user_key	= USR.id_user_mod_key
		WHERE	USR.id_user_key			= ISNULL(@id_user_key			, USR.id_user_key)
			AND		USR.id_group_key			= ISNULL(@id_group_key			, USR.id_group_key)
			AND		USR.tx_first_name			= ISNULL(@tx_first_name			, USR.tx_first_name)
			AND		USR.tx_last_name			= ISNULL(@tx_last_name			, USR.tx_last_name)
			AND		USR.tx_login_name			= ISNULL(@tx_login_name			, USR.tx_login_name)
			AND		CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) = ISNULL(@tx_password	, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)))
			---AND		USR.tx_password			= ISNULL(@tx_password			, USR.tx_password)
			AND		USR.tx_user_alias			= ISNULL(@tx_user_alias			, USR.tx_user_alias)
			AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
				--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
			AND		GRP.tx_group_name			= ISNULL(@tx_group_name			, GRP.tx_group_name)
			AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
				--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)

/*
		SELECT	  tx_rs_type = 'RS_TYPE_USER'
				, USR.*	
				, id_branch_key	= GM.id_from_key
		FROM	T_USER						USR
		LEFT	OUTER JOIN V_GENERIC_MAP	GM	ON	USR.id_user_key		= GM.id_to_key
												AND	GM.from_type_name	= 'BRANCH'
												AND GM.to_type_Name		= 'USER'
		WHERE	USR.id_user_key			= ISNULL(@id_user_key			, USR.id_user_key)
			AND		USR.id_group_key			= ISNULL(@id_group_key			, USR.id_group_key)
			AND		USR.tx_first_name			= ISNULL(@tx_first_name			, USR.tx_first_name)
			AND		USR.tx_last_name			= ISNULL(@tx_last_name			, USR.tx_last_name)
			AND		USR.tx_login_name			= ISNULL(@tx_login_name			, USR.tx_login_name)
			AND		CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) = ISNULL(@tx_password	, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)))
			---AND		USR.tx_password			= ISNULL(@tx_password			, USR.tx_password)
			AND		USR.tx_user_alias			= ISNULL(@tx_user_alias			, USR.tx_user_alias)
			AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
				--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
*/
	END
	ELSE
	BEGIN
		-- AS AT Request
		SELECT	  tx_rs_type = 'RS_TYPE_USER'
				, USR.*
				, ENV.id_env_key
				, UMOD.tx_login_name	as tx_user_mod_name
				, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) AS tx_password_ref
--				
		FROM	T_USER_AUDIT	USR
		JOIN	(
					SELECT	id_user_key, MAX(id_user_ver) AS id_user_ver
					FROM	T_USER_AUDIT USR
					WHERE	USR.id_user_key			= ISNULL(@id_user_key			, USR.id_user_key)
						AND		USR.id_group_key			= ISNULL(@id_group_key			, USR.id_group_key)
						AND		USR.tx_first_name			= ISNULL(@tx_first_name			, USR.tx_first_name)
						AND		USR.tx_last_name			= ISNULL(@tx_last_name			, USR.tx_last_name)
						AND		USR.tx_login_name			= ISNULL(@tx_login_name			, USR.tx_login_name)
						AND		CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) = ISNULL(@tx_password	, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)))
						---AND		USR.tx_password			= ISNULL(@tx_password			, USR.tx_password)
						AND		USR.tx_user_alias			= ISNULL(@tx_user_alias			, USR.tx_user_alias)
						AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
							--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
					AND		USR.dtt_mod <= @dtt_as_at
					GROUP	BY id_user_key
				)				V		ON (V.id_user_key			= USR.id_user_key	AND 
											V.id_user_ver			= USR.id_user_ver)
		JOIN	T_GROUP			GRP		ON	GRP.id_group_key		= USR.id_group_key
		JOIN	T_ENVIRONMENT	ENV		ON	ENV.id_env_key			 = USR.id_env_key
		JOIN	T_USER			UMOD	ON	UMOD.id_user_key		= USR.id_user_mod_key
		WHERE	USR.id_user_key			= ISNULL(@id_user_key			, USR.id_user_key)
			AND		USR.id_group_key			= ISNULL(@id_group_key			, USR.id_group_key)
			AND		USR.tx_first_name			= ISNULL(@tx_first_name			, USR.tx_first_name)
			AND		USR.tx_last_name			= ISNULL(@tx_last_name			, USR.tx_last_name)
			AND		USR.tx_login_name			= ISNULL(@tx_login_name			, USR.tx_login_name)
			AND		CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)) = ISNULL(@tx_password	, CONVERT(varchar, DECRYPTBYPASSPHRASE(CONVERT(varchar, USR.id_user_key), USR.tx_password)))
			---AND		USR.tx_password			= ISNULL(@tx_password			, USR.tx_password)
			AND		USR.tx_user_alias			= ISNULL(@tx_user_alias			, USR.tx_user_alias)
			AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
				--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
			AND		GRP.tx_group_name			= ISNULL(@tx_group_name			, GRP.tx_group_name)
			AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
				--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
			AND		ENV.is_active		= ISNULL(@is_active		, ENV.is_active)
				--	AND		ENV.id_env_key	= ISNULL(@id_env_key	, ENV.id_env_key)
	END
							
	IF (@is_full_details = 1)
	BEGIN
		-- User Groups
		SELECT	tx_rs_type = 'RS_TYPE_GROUP', GRP.*
		FROM	V_USER_GROUP	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
			--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
			--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
		

		-- Roles
		SELECT	tx_rs_type = 'RS_TYPE_ROLE', ROLE.*
		FROM	V_GROUP_ROLE		ROLE
		JOIN	V_USER_GROUP		GRP		ON GRP.id_group_key = ROLE.id_group_key
		JOIN	T_USER				USR		ON USR.id_user_key	= GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
			--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
			--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
		AND		ROLE.is_active		= ISNULL(@is_active		, ROLE.is_active)
			--	AND		ROLE.id_env_key	= ISNULL(@id_env_key	, ROLE.id_env_key)

		--- Swift mt

		SELECT	tx_rs_type = 'RS_TYPE_SWIFT_MODEL_MT_IN', GRP.*
		FROM	V_USER_SWIFT_MT_IN	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
			--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
			--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)

		SELECT	tx_rs_type = 'RS_TYPE_SWIFT_MODEL_MT_OUT', GRP.*
		FROM	V_USER_SWIFT_MT_OUT	GRP
		JOIN	T_USER			USR	ON	USR.id_user_key = GRP.id_user_key
		WHERE	USR.id_user_key		= ISNULL(@id_user_key	, USR.id_user_key)
		AND		USR.tx_login_name	= ISNULL(@tx_login_name	, USR.tx_login_name)
		AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
			--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
		AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
			--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
		
	END


	RETURN 0
