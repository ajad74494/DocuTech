
/****** Object:  StoredProcedure [dbo].[SEL_group]    Script Date: 18/12/2019 12:43:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

	CREATE PROC [dbo].[SEL_group]
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
	, @id_group_key				int								= NULL	
	, @id_group_ver					int								= NULL
	, @tx_group_name			varchar(64)			= NULL
	, @tx_desc					varchar(2048)				= NULL  

	, @id_user_key				int								= NULL      
	, @id_legal_entity_key		int								= NULL
	, @tx_legal_entity_name		varchar(128)	= NULL
	, @tx_first_name			varchar(64)			= NULL      
	, @tx_last_name				varchar(64)			= NULL      
	, @tx_login_name			varchar(32)			= NULL      
	, @tx_password				varchar(8000)			= NULL      
	, @tx_user_alias			varchar(32)			= NULL      
	
	, @is_full_details			bit								= 0
	 
	 , @is_sel_data			int						= NULL
	, @tx_log_level			varchar(32)			= NULL
	, @id_log_level			int						= NULL
	, @is_record_time		bit						= NULL
	, @is_print_msg			bit						= NULL
	, @is_persist_msg		bit						= NULL
	, @tx_json_log_msg		varchar(MAX)	= NULL		OUTPUT

AS
BEGIN	
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
		EXEC @g_id_return_status = GET_sp_log_level @tx_sp_name='SEL_group', @tx_log_level=@tx_log_level OUTPUT, @id_log_level=@id_log_level OUTPUT, @is_record_time=@is_record_time OUTPUT, @is_print_msg=@is_print_msg OUTPUT, @is_persist_msg=@is_persist_msg OUTPUT
	END

	SELECT @g_is_record_time = @is_record_time, @g_is_print_msg = @is_print_msg, @g_is_persist_msg	= @is_persist_msg, @g_id_log_level = @id_log_level

	SELECT @g_tx_err_msg = 'Entering[SEL_group] : @tx_action[' + ISNULL(@tx_action_name, '?') + ']'
	SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'SEL_group.sp', @g_id_line_num = 138

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

	IF (  (NOT  ((@id_group_key IS NULL) OR (@id_group_key = -2147483648)))  AND ( NOT ((@id_group_ver IS NULL) OR (@id_group_ver = -2147483648))) )
	BEGIN
		-- Assumption is that it is a version history request, ensure its not the latest version that is being requesting
		IF ( NOT EXISTS (SELECT 1 FROM T_GROUP WHERE id_group_key = @id_group_key AND id_group_ver = @id_group_ver) )
		BEGIN
			-- A historic request......
			SELECT	  @dtt_as_at = dtt_mod
					, @is_full_details = 1
			FROM	T_GROUP_AUDIT	A
			WHERE	id_group_key 	= @id_group_key
			AND		id_group_ver		= @id_group_ver
			AND		is_active		= ISNULL(@is_active		, is_active)
				--	AND		id_env_key		= ISNULL(@id_env_key	, id_env_key)

			SELECT @g_tx_log_msg  = 'Historical data request: @id_group_ver[' + CONVERT(varchar, @id_group_ver) + '] @dtt_as_at[' + CONVERT(varchar, @dtt_as_at) + ']'
			SELECT @g_dtt_log = GETDATE(), @g_tx_sp_name = 'SEL_group.sp', @g_id_line_num = 261
		
			-- TODO_H : [Naz] Move to Function
		
			IF ( NOT (((@g_id_log_level IS NULL) OR (@g_id_log_level = -2147483648))) )
			BEGIN
				IF (2 >= @g_id_log_level)
				BEGIN
					IF (@g_is_print_msg = 1)
					BEGIN
						SELECT @g_tx_tmp_log_msg = '[' + @g_tx_sp_name + ':' + CONVERT(varchar, @g_id_line_num) + '] | ' + CONVERT(varchar, @g_dtt_log) + ' | ' + 'INFO ' + ' | ' + '?' + ' | [' + CONVERT(varchar, 0) + '] -> ' + ISNULL(@g_tx_log_msg, '?')
						PRINT  @g_tx_tmp_log_msg
					END
		
					IF ( (@g_is_persist_msg = 1) )
					BEGIN
						EXEC @g_id_return_status = ACT_msg_log   @tx_action_name = 'NEW', @id_env_key = @g_id_env_key, @id_user_mod_key = @id_user_mod_key, @dtt_mod = @g_dtt_log
															   , @id_state_key = @id_state_key, @id_action_key = @id_action_key, @tx_log_msg_type='?', @tx_log_level='INFO ', @id_log_level=2
															   , @tx_sp_name=@g_tx_sp_name, @id_line_num=@g_id_line_num, @id_err_code=0, @tx_log_msg=@g_tx_log_msg, @tx_json_log_msg = @tx_json_log_msg OUTPUT
					
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
			END;
		END
	END
	
	IF (@is_full_details = 0)
	BEGIN
		SELECT	  tx_rs_type = 'RS_TYPE_GROUP'	
				, GRP.*
				, U.tx_login_name 
		FROM    T_GROUP			GRP
		JOIN	T_USER			U	ON	GRP.id_user_mod_key = U.id_user_key
		WHERE	GRP.id_group_key			= ISNULL(@id_group_key			, GRP.id_group_key)
			AND		GRP.tx_group_name			= ISNULL(@tx_group_name			, GRP.tx_group_name)
			AND		GRP.tx_desc				= ISNULL(@tx_desc				, GRP.tx_desc)
			AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
				--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
		ORDER	BY GRP.tx_group_name
	END
	ELSE	-- FULL Details or AS AT
	BEGIN
		SELECT @dtt_last_refresh = '01/01/1970'

		-- Create TMP Table

		SELECT	  tx_rs_type = 'RS_TYPE_GROUP'	
				, GRP.*
				, U.tx_login_name
		INTO	#TMP_GROUP
		FROM    T_GROUP			GRP
		JOIN	T_USER			U	ON	GRP.id_user_mod_key = U.id_user_key
		WHERE	1=2

		
		IF ( ((@dtt_as_at IS NULL) OR (@dtt_as_at = '01/01/1970')) )
		BEGIN
			INSERT	INTO #TMP_GROUP
			SELECT	  tx_rs_type = 'RS_TYPE_GROUP'	
					, GRP.*
					, U.tx_login_name 
			FROM    T_GROUP			GRP
			JOIN	T_USER			U	ON	GRP.id_user_mod_key = U.id_user_key
			WHERE	GRP.id_group_key			= ISNULL(@id_group_key			, GRP.id_group_key)
				AND		GRP.tx_group_name			= ISNULL(@tx_group_name			, GRP.tx_group_name)
				AND		GRP.tx_desc				= ISNULL(@tx_desc				, GRP.tx_desc)
				AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
					--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
			ORDER	BY GRP.tx_group_name


			SELECT * FROM #TMP_GROUP

			-- Users
			SELECT	tx_rs_type = 'RS_TYPE_USER'
					, USR.*				
			FROM	#TMP_GROUP		TMP
			JOIN	V_USER_GROUP	GRP		ON	GRP.id_group_key	= TMP.id_group_key
			JOIN	T_USER			USR		ON	USR.id_user_key		= GRP.id_user_key
			--WHERE	GRP.id_group_key		= ISNULL(@id_group_key	, GRP.id_group_key)
			WHERE	USR.tx_login_name		= ISNULL(@tx_login_name	, USR.tx_login_name)
			AND		USR.id_user_key			= ISNULL(@id_user_key	, USR.id_user_key)
			AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
				--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
			AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
				--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
			ORDER	BY USR.tx_login_name
			

			-- Roles
			SELECT	 tx_rs_type = 'RS_TYPE_ROLE'
					, R.*
			FROM	#TMP_GROUP		TMP
			JOIN	V_GROUP_ROLE	ROLE	ON ROLE.id_group_key = TMP.id_group_key
			JOIN	T_ROLE			R		ON ROLE.id_role_key	= R.id_role_key
			WHERE		ROLE.is_active		= ISNULL(@is_active		, ROLE.is_active)
				--	AND		ROLE.id_env_key	= ISNULL(@id_env_key	, ROLE.id_env_key)
			ORDER	BY ROLE.tx_role_name
			
		END
		ELSE -- AS AT REQUEST
		BEGIN
			
			IF (NOT ((@id_group_ver IS NULL) OR (@id_group_ver = -2147483648)))
			BEGIN
				INSERT	INTO #TMP_GROUP
				SELECT	  tx_rs_type = 'RS_TYPE_GROUP'	
						, GRP.*
						, U.tx_login_name 
				FROM    T_GROUP_AUDIT	GRP
				JOIN	T_USER			U	ON	GRP.id_user_mod_key = U.id_user_key
				WHERE	GRP.id_group_key			= ISNULL(@id_group_key			, GRP.id_group_key)
					AND		GRP.tx_group_name			= ISNULL(@tx_group_name			, GRP.tx_group_name)
					AND		GRP.tx_desc				= ISNULL(@tx_desc				, GRP.tx_desc)
					AND		GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
						--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
				AND		GRP.id_group_ver = @id_group_ver
				ORDER	BY GRP.tx_group_name

			END
			ELSE
			BEGIN
				SELECT g_tx_err_msg = 'Unimplemented Function'
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
			
					IF (99999 = 0)
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
			
				RETURN 99999
			END

			SELECT * FROM #TMP_GROUP

			-- USER
			SELECT	tx_rs_type = 'RS_TYPE_USER'
					, USR.*				
			FROM	V_USER_GROUP_AUDIT	GRP
			JOIN	(
						SELECT	VUGA.id_generic_map_key, MAX(VUGA.id_generic_map_ver) AS id_generic_map_ver
						FROM	V_USER_GROUP_AUDIT	VUGA
						JOIN	#TMP_GROUP			GRP		ON GRP.id_group_key = VUGA.id_group_key
						WHERE	VUGA.dtt_mod <= @dtt_as_at
						GROUP	BY VUGA.id_generic_map_key
					)					V		ON	(V.id_generic_map_key = GRP.id_generic_map_key AND V.id_generic_map_ver = GRP.id_generic_map_ver)
			JOIN	T_USER				USR		ON	USR.id_user_key	= GRP.id_user_key
			WHERE	GRP.is_active		= ISNULL(@is_active		, GRP.is_active)
			--	AND		GRP.id_env_key	= ISNULL(@id_env_key	, GRP.id_env_key)
			AND		USR.is_active		= ISNULL(@is_active		, USR.is_active)
				--	AND		USR.id_env_key	= ISNULL(@id_env_key	, USR.id_env_key)
			ORDER	BY USR.tx_login_name
			
			
			-- ROLE
			SELECT	 tx_rs_type = 'RS_TYPE_ROLE'
					, R.*
			FROM	V_GROUP_ROLE_AUDIT	VROLE
			JOIN	(
						SELECT	VGRA.id_generic_map_key, MAX(VGRA.id_generic_map_ver) AS id_generic_map_ver
						FROM	V_GROUP_ROLE_AUDIT	VGRA
						JOIN	#TMP_GROUP			GRP		ON GRP.id_group_key = VGRA.id_group_key
						WHERE	VGRA.dtt_mod <= @dtt_as_at
						GROUP	BY VGRA.id_generic_map_key
					 )					V		ON (V.id_generic_map_key = VROLE.id_generic_map_key AND V.id_generic_map_ver = VROLE.id_generic_map_ver)
			JOIN	T_ROLE				R		ON R.id_role_key	= VROLE.id_role_key	
			WHERE	VROLE.is_active		= ISNULL(@is_active		, VROLE.is_active)
			--	AND		VROLE.id_env_key	= ISNULL(@id_env_key	, VROLE.id_env_key)
			AND		R.is_active		= ISNULL(@is_active		, R.is_active)
				--	AND		R.id_env_key	= ISNULL(@id_env_key	, R.id_env_key)
			ORDER	BY R.tx_role_name
		END

		DROP TABLE #TMP_GROUP
	END
		
	RETURN 0
END
